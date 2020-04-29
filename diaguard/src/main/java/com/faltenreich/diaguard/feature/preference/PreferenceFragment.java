package com.faltenreich.diaguard.feature.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.preference.Preference;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.export.job.Export;
import com.faltenreich.diaguard.feature.export.job.ExportCallback;
import com.faltenreich.diaguard.shared.SystemUtils;
import com.faltenreich.diaguard.shared.data.async.DataLoader;
import com.faltenreich.diaguard.shared.data.async.DataLoaderListener;
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

public class PreferenceFragment extends BasePreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private ProgressComponent progressComponent = new ProgressComponent();

    public PreferenceFragment() {
        super(R.xml.preferences, R.string.settings);
    }

    @Override
    public void onResume() {
        super.onResume();
        Events.register(this);
    }

    @Override
    public void onDestroy() {
        Events.unregister(this);
        super.onDestroy();
    }

    @Override
    protected void onSummarySet(Preference preference) {
        super.onSummarySet(preference);

        if (preference.getKey() != null) {
            String key = preference.getKey();
            switch (key) {
                case "version":
                    preference.setSummary(SystemUtils.getVersionName(requireActivity()));
                    break;
                case "categories":
                    int activeCategoriesCount = PreferenceHelper.getInstance().getActiveCategories().length;
                    int categoriesTotalCount = Category.values().length;
                    preference.setSummary(String.format("%d/%d %s",
                        activeCategoriesCount,
                        categoriesTotalCount,
                        getString(R.string.active)));
                    break;
                case "tags":
                    DataLoader.getInstance().load(preference.getContext(), new DataLoaderListener<Long>() {
                        @Override
                        public Long onShouldLoad() {
                            return TagDao.getInstance().countAll();
                        }

                        @Override
                        public void onDidLoad(Long count) {
                            if (isAdded()) {
                                preference.setSummary(String.format(getString(R.string.available_placeholder), count));
                            }
                        }
                    });
                    break;
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        super.onSharedPreferenceChanged(sharedPreferences, key);

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