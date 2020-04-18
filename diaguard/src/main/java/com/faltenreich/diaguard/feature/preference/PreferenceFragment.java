package com.faltenreich.diaguard.feature.preference;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.DialogFragment;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.export.job.Export;
import com.faltenreich.diaguard.feature.export.job.ExportCallback;
import com.faltenreich.diaguard.feature.preference.bloodsugar.BloodSugarPreference;
import com.faltenreich.diaguard.feature.preference.bloodsugar.BloodSugarPreferenceDialogFragment;
import com.faltenreich.diaguard.feature.preference.factor.FactorPreference;
import com.faltenreich.diaguard.feature.preference.factor.FactorPreferenceDialogFragment;
import com.faltenreich.diaguard.shared.SystemUtils;
import com.faltenreich.diaguard.shared.data.async.DataLoader;
import com.faltenreich.diaguard.shared.data.async.DataLoaderListener;
import com.faltenreich.diaguard.shared.data.database.dao.TagDao;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.file.FileUtils;
import com.faltenreich.diaguard.shared.data.permission.Permission;
import com.faltenreich.diaguard.shared.data.preference.PreferenceHelper;
import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;
import com.faltenreich.diaguard.shared.event.Events;
import com.faltenreich.diaguard.shared.event.file.BackupImportedEvent;
import com.faltenreich.diaguard.shared.event.file.FileProvidedEvent;
import com.faltenreich.diaguard.shared.event.file.FileProvidedFailedEvent;
import com.faltenreich.diaguard.shared.event.permission.PermissionResponseEvent;
import com.faltenreich.diaguard.shared.event.preference.MealFactorUnitChangedEvent;
import com.faltenreich.diaguard.shared.event.preference.UnitChangedEvent;
import com.faltenreich.diaguard.shared.view.activity.BaseActivity;
import com.faltenreich.diaguard.shared.view.progress.ProgressComponent;
import com.faltenreich.diaguard.shared.view.resource.ColorUtils;
import com.faltenreich.diaguard.shared.view.theme.Theme;
import com.faltenreich.diaguard.shared.view.theme.ThemeUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;

public class PreferenceFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private ProgressComponent progressComponent = new ProgressComponent();

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        init();
        initLayout();
    }

    @Override
    public void onResume() {
        super.onResume();
        Events.register(this);
        setSummaries();
    }

    @Override
    public void onDestroy() {
        Events.unregister(this);
        super.onDestroy();
    }

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        DialogFragment fragment = null;
        if (preference instanceof BloodSugarPreference) {
            fragment = BloodSugarPreferenceDialogFragment.newInstance(preference.getKey());
        } else if (preference instanceof FactorPreference) {
            fragment = FactorPreferenceDialogFragment.newInstance(preference.getKey());
        }
        if (fragment != null) {
            fragment.setTargetFragment(this, 0);
            fragment.show(getParentFragmentManager(), fragment.getClass().getName());
        } else {
            super.onDisplayPreferenceDialog(preference);
        }
    }

    private void init() {
        addPreferencesFromResource(R.xml.preferences);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity());
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    private void initLayout() {
        int color = ColorUtils.getPrimaryColor(getContext());
        applyThemeToIcons(getPreferenceScreen(), color);
    }

    private ArrayList<Preference> getPreferenceList(Preference preference, ArrayList<Preference> list) {
        if (preference instanceof PreferenceCategory || preference instanceof PreferenceScreen) {
            PreferenceGroup pGroup = (PreferenceGroup) preference;
            int pCount = pGroup.getPreferenceCount();
            for (int i = 0; i < pCount; i++) {
                getPreferenceList(pGroup.getPreference(i), list);
            }
        } else {
            list.add(preference);
        }
        return list;
    }

    private void applyThemeToIcons(Preference preference, @ColorInt int color) {
        Drawable icon = preference.getIcon();
        if (icon != null) {
            DrawableCompat.setTint(icon, color);
            preference.setIcon(icon);
        }

        if (preference instanceof PreferenceGroup) {
            PreferenceGroup group = ((PreferenceGroup) preference);
            for (int index = 0; index < group.getPreferenceCount(); index++) {
                applyThemeToIcons(group.getPreference(index), color);
            }
        }
    }

    private void setSummaries() {
        for (Preference preference : getPreferenceList(getPreferenceScreen(), new ArrayList<>())) {
            setSummary(preference);
        }
    }

    @SuppressLint("DefaultLocale")
    private void setSummary(final Preference preference) {
        if (isAdded() && preference != null) {
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                preference.setSummary(listPreference.getEntry());

            } else if (preference instanceof BloodSugarPreference) {
                String value = PreferenceHelper.getInstance().getValueForKey(preference.getKey());
                float number = FloatUtils.parseNumber(value);
                if (number > 0) {
                    if (getActivity() != null) {
                        int descriptionResId = getResources().getIdentifier(preference.getKey() + "_desc", "string", getActivity().getPackageName());
                        String description = descriptionResId > 0 ? getString(descriptionResId) + " " : "";
                        number = PreferenceHelper.getInstance().formatDefaultToCustomUnit(Category.BLOODSUGAR, number);
                        value = FloatUtils.parseFloat(number);
                        preference.setSummary(description + value + " " + PreferenceHelper.getInstance().getUnitAcronym(Category.BLOODSUGAR));
                    }
                } else {
                    preference.setSummary(null);
                }

            } else if (preference.getKey() != null) {
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
        setSummary(findPreference(key));
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