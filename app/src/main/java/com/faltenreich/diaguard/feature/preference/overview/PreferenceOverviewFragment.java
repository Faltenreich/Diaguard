package com.faltenreich.diaguard.feature.preference.overview;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.preference.Preference;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.export.job.Export;
import com.faltenreich.diaguard.feature.export.job.ExportCallback;
import com.faltenreich.diaguard.feature.preference.PreferenceFragment;
import com.faltenreich.diaguard.feature.preference.backup.BackupImportPreference;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.SystemUtils;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.file.FileUtils;
import com.faltenreich.diaguard.shared.data.permission.Permission;
import com.faltenreich.diaguard.shared.event.Events;
import com.faltenreich.diaguard.shared.event.file.BackupImportedEvent;
import com.faltenreich.diaguard.shared.event.file.FileProvidedEvent;
import com.faltenreich.diaguard.shared.event.file.FileProvidedFailedEvent;
import com.faltenreich.diaguard.shared.event.permission.PermissionResponseEvent;
import com.faltenreich.diaguard.shared.event.preference.MealFactorUnitChangedEvent;
import com.faltenreich.diaguard.shared.event.preference.UnitChangedEvent;
import com.faltenreich.diaguard.shared.view.progress.ProgressComponent;
import com.faltenreich.diaguard.shared.view.theme.Theme;
import com.faltenreich.diaguard.shared.view.theme.ThemeUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

public class PreferenceOverviewFragment
    extends PreferenceFragment
    implements SharedPreferences.OnSharedPreferenceChangeListener {

    private final ProgressComponent progressComponent = new ProgressComponent();

    public PreferenceOverviewFragment() {
        super(R.xml.preferences_overview, R.string.settings);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        super.onCreatePreferences(savedInstanceState, rootKey);
        getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        setSummaryForVersion();
    }

    @Override
    public void onResume() {
        super.onResume();
        Events.register(this);
    }

    @Override
    public void onDestroyView() {
        Events.unregister(this);
        getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroyView();
    }

    private void setSummaryForVersion() {
        Preference preference = requirePreference(getString(R.string.preference_version));
        preference.setSummaryProvider(pref -> SystemUtils.getVersionName(requireActivity()));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (!isAdded()) {
            return;
        }
        if (key.equals(getString(R.string.preference_unit_bloodsugar))) {
            Events.post(new UnitChangedEvent(Category.BLOODSUGAR));
        } else if (key.equals(getString(R.string.preference_unit_meal))) {
            Events.post(new UnitChangedEvent(Category.MEAL));
        } else if (key.equals(getString(R.string.preference_unit_meal_factor))) {
            Events.post(new MealFactorUnitChangedEvent());
        } else if (key.equals(getString(R.string.preference_unit_hba1c))) {
            Events.post(new UnitChangedEvent(Category.HBA1C));
        } else if (key.equals(getString(R.string.preference_unit_weight))) {
            Events.post(new UnitChangedEvent(Category.WEIGHT));
        } else if (key.equals(getString(R.string.preference_theme))) {
            Theme theme = PreferenceStore.getInstance().getTheme();
            ThemeUtils.setDefaultNightMode(theme);
            ThemeUtils.setUiMode(getActivity(), theme);
        }
    }

    private void createBackup() {
        Context context = requireActivity();
        progressComponent.show(context);

        ExportCallback callback = new ExportCallback() {
            @Override
            public void onProgress(String message) {
                progressComponent.setMessage(message);
            }

            @Override
            public void onSuccess(@Nullable File file, String mimeType) {
                progressComponent.dismiss();
                if (file != null && getContext() != null) {
                    FileUtils.shareFile(getContext(), file, R.string.backup_store);
                } else {
                    onError();
                }
            }

            @Override
            public void onError() {
                progressComponent.dismiss();
                Toast.makeText(getActivity(), getString(R.string.error_unexpected), Toast.LENGTH_SHORT).show();
            }
        };
        Export.exportCsv(context, callback);
    }

    private void importBackup(Uri uri) {
        Context context = requireActivity();
        progressComponent.show(context);

        Export.importCsv(context, uri, new ExportCallback() {

            @Override
            public void onProgress(String message) {
                progressComponent.setMessage(message);
            }

            @Override
            public void onSuccess(@Nullable File file, String mimeType) {
                progressComponent.dismiss();
                Toast.makeText(getActivity(), getString(R.string.backup_complete), Toast.LENGTH_SHORT).show();
                Events.post(new BackupImportedEvent());
            }

            @Override
            public void onError() {
                progressComponent.dismiss();
                Toast.makeText(getActivity(), getString(R.string.error_import), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FileProvidedEvent event) {
        importBackup(event.context);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FileProvidedFailedEvent event) {
        Toast.makeText(getActivity(), getString(R.string.error_unexpected), Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PermissionResponseEvent event) {
        if (event.context == Permission.WRITE_EXTERNAL_STORAGE && event.isGranted) {
            switch (event.useCase) {
                case BACKUP_WRITE:
                    createBackup();
                    break;
                case BACKUP_READ:
                    if (getActivity() != null) {
                        String mimeType = "text/*"; // Workaround: text/csv does not work for all apps
                        FileUtils.searchFiles(getActivity(), mimeType, BackupImportPreference.REQUEST_CODE_BACKUP_IMPORT);
                    }
                    break;
            }
        }
    }
}