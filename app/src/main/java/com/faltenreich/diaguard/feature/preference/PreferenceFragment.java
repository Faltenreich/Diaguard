package com.faltenreich.diaguard.feature.preference;

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
import com.faltenreich.diaguard.shared.SystemUtils;
import com.faltenreich.diaguard.shared.data.database.dao.TagDao;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.file.FileUtils;
import com.faltenreich.diaguard.shared.data.permission.Permission;
import com.faltenreich.diaguard.shared.data.preference.PreferenceHelper;
import com.faltenreich.diaguard.shared.event.Events;
import com.faltenreich.diaguard.shared.event.file.BackupImportedEvent;
import com.faltenreich.diaguard.shared.event.file.FileProvidedEvent;
import com.faltenreich.diaguard.shared.event.file.FileProvidedFailedEvent;
import com.faltenreich.diaguard.shared.event.permission.PermissionResponseEvent;
import com.faltenreich.diaguard.shared.event.preference.MealFactorUnitChangedEvent;
import com.faltenreich.diaguard.shared.event.preference.UnitChangedEvent;
import com.faltenreich.diaguard.shared.view.activity.BaseActivity;
import com.faltenreich.diaguard.shared.view.progress.ProgressComponent;
import com.faltenreich.diaguard.shared.view.theme.Theme;
import com.faltenreich.diaguard.shared.view.theme.ThemeUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.Locale;

public class PreferenceFragment extends BasePreferenceFragment
    implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String KEY_VERSION = "version";
    private static final String KEY_CATEGORIES = "categories";
    private static final String KEY_TAGS = "tags";

    private ProgressComponent progressComponent = new ProgressComponent();

    public PreferenceFragment() {
        super(R.xml.preferences, R.string.settings);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        super.onCreatePreferences(savedInstanceState, rootKey);
        getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Events.register(this);
        setSummaries();
    }

    @Override
    public void onDestroyView() {
        Events.unregister(this);
        getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroyView();
    }

    private void setSummaries() {
        setSummaryForVersion();
        setSummaryForCategories();
        setSummaryForTags();
    }

    private void setSummaryForVersion() {
        Preference preference = requirePreference(KEY_VERSION);
        preference.setSummaryProvider(pref -> SystemUtils.getVersionName(requireActivity()));
    }

    private void setSummaryForCategories() {
        Preference preference = requirePreference(KEY_CATEGORIES);
        preference.setSummaryProvider(pref -> String.format(
            Locale.getDefault(),
            "%d/%d %s",
            PreferenceHelper.getInstance().getActiveCategories().length,
            Category.values().length,
            getString(R.string.active)
        ));
    }

    private void setSummaryForTags() {
        Preference preference = requirePreference(KEY_TAGS);
        preference.setSummaryProvider(pref -> String.format(
            Locale.getDefault(),
            getString(R.string.available_placeholder),
            TagDao.getInstance().countAll()
        ));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.endsWith("_active")) {
            key = "categories";
        }
        switch (key) {
            case "unit_bloodsugar":
                Events.post(new UnitChangedEvent(Category.BLOODSUGAR));
                break;
            case "unit_meal":
                Events.post(new UnitChangedEvent(Category.MEAL));
                break;
            case "unit_meal_factor":
                Events.post(new MealFactorUnitChangedEvent());
                break;
            case "unit_hba1c":
                Events.post(new UnitChangedEvent(Category.HBA1C));
                break;
            case "unit_weight":
                Events.post(new UnitChangedEvent(Category.WEIGHT));
                break;
            case PreferenceHelper.Keys.THEME:
                Theme theme = PreferenceHelper.getInstance().getTheme();
                ThemeUtils.setDefaultNightMode(theme);
                ThemeUtils.setUiMode(getActivity(), theme);
                break;
        }
    }

    private void createBackup() {
        Context context = getActivity();
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
        progressComponent.show(getActivity());
        Export.importCsv(getActivity(), uri, new ExportCallback() {

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

    @SuppressWarnings("unused")
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
                        FileUtils.searchFiles(getActivity(), mimeType, BaseActivity.REQUEST_CODE_BACKUP_IMPORT);
                    }
                    break;
            }
        }
    }
}