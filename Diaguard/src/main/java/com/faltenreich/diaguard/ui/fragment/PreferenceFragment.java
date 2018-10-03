package com.faltenreich.diaguard.ui.fragment;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.faltenreich.diaguard.BuildConfig;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.async.DataLoader;
import com.faltenreich.diaguard.data.async.DataLoaderListener;
import com.faltenreich.diaguard.data.dao.TagDao;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.event.BackupImportedEvent;
import com.faltenreich.diaguard.event.Events;
import com.faltenreich.diaguard.event.FileProvidedEvent;
import com.faltenreich.diaguard.event.FileProvidedFailedEvent;
import com.faltenreich.diaguard.event.PermissionResponseEvent;
import com.faltenreich.diaguard.event.preference.MealFactorUnitChangedEvent;
import com.faltenreich.diaguard.event.preference.UnitChangedEvent;
import com.faltenreich.diaguard.ui.activity.BaseActivity;
import com.faltenreich.diaguard.ui.view.preferences.BloodSugarPreference;
import com.faltenreich.diaguard.ui.view.preferences.CategoryPreference;
import com.faltenreich.diaguard.util.FileUtils;
import com.faltenreich.diaguard.util.Helper;
import com.faltenreich.diaguard.util.NumberUtils;
import com.faltenreich.diaguard.util.ProgressComponent;
import com.faltenreich.diaguard.util.SystemUtils;
import com.faltenreich.diaguard.util.export.Export;
import com.faltenreich.diaguard.util.export.FileListener;
import com.faltenreich.diaguard.util.permission.Permission;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;

// android.support.v7.preference has currently an incompatible API
public class PreferenceFragment extends android.preference.PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String EXTRA_OPENING_PREFERENCE = "EXTRA_OPENING_PREFERENCE";

    private ProgressComponent progressComponent = new ProgressComponent();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        initPreferences();
        checkIntents();
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

    private ArrayList<Preference> getPreferenceList(Preference preference, ArrayList<Preference> list) {
        if (preference instanceof PreferenceCategory || preference instanceof PreferenceScreen) {
            PreferenceGroup pGroup = (PreferenceGroup) preference;
            int pCount = pGroup.getPreferenceCount();
            for (int i = 0; i < pCount; i++) {
                getPreferenceList(pGroup.getPreference(i), list); // recursive call
            }
        } else {
            list.add(preference);
        }
        return list;
    }

    private void initPreferences() {
        if (!BuildConfig.isCalculatorEnabled) {
            Preference categoryPreferenceLimits = findPreference("limits");
            if (categoryPreferenceLimits != null && categoryPreferenceLimits instanceof PreferenceCategory) {
                PreferenceCategory category = (PreferenceCategory) categoryPreferenceLimits;
                category.removePreference(findPreference("correction_value"));
                category.removePreference(findPreference("pref_factor"));
            }
            Preference categoryPreferenceUnits = findPreference("units");
            if (categoryPreferenceUnits != null && categoryPreferenceUnits instanceof PreferenceCategory) {
                PreferenceCategory category = (PreferenceCategory) categoryPreferenceUnits;
                category.removePreference(findPreference("unit_meal_factor"));
            }
        }
    }

    private void setSummaries() {
        for (Preference preference : getPreferenceList(getPreferenceScreen(), new ArrayList<Preference>())) {
            setSummary(preference);
        }
    }

    private void setSummary(final Preference preference) {
        if (isAdded() && preference != null) {
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                preference.setSummary(listPreference.getEntry());

            } else if (preference instanceof CategoryPreference) {
                int activeCategoriesCount = PreferenceHelper.getInstance().getActiveCategories().length;
                int categoriesTotalCount = Measurement.Category.values().length;
                preference.setSummary(String.format("%d/%d %s",
                        activeCategoriesCount,
                        categoriesTotalCount,
                        getString(R.string.active)));

            } else if (preference instanceof BloodSugarPreference) {
                String value = PreferenceHelper.getInstance().getValueForKey(preference.getKey());
                float number = NumberUtils.parseNumber(value);
                if (number > 0) {
                    if (getActivity() != null) {
                        int descriptionResId = getResources().getIdentifier(preference.getKey() + "_desc", "string", getActivity().getPackageName());
                        String description = descriptionResId > 0 ? getString(descriptionResId) + " " : "";
                        number = PreferenceHelper.getInstance().formatDefaultToCustomUnit(Measurement.Category.BLOODSUGAR, number);
                        value = Helper.parseFloat(number);
                        preference.setSummary(description + value + " " + PreferenceHelper.getInstance().getUnitAcronym(Measurement.Category.BLOODSUGAR));
                    }
                } else {
                    preference.setSummary(null);
                }

            } else if (preference.getKey() != null) {
                String key = preference.getKey();
                if (key.equals("version")) {
                    preference.setSummary(SystemUtils.getVersionName(getActivity()));
                } else if (key.equals("tags")) {
                    DataLoader.getInstance().load(preference.getContext(), new DataLoaderListener<Long>() {
                        @Override
                        public Long onShouldLoad() {
                            return TagDao.getInstance().countAll();
                        }
                        @Override
                        public void onDidLoad(Long count) {
                            preference.setSummary(String.format(getString(R.string.available_placeholder), count));
                        }
                    });
                }
            }
        }
    }

    private void checkIntents() {
        if (getActivity() != null && getActivity().getIntent() != null && getActivity().getIntent().getExtras() != null) {
            Bundle extras = getActivity().getIntent().getExtras();
            if (extras.getString(EXTRA_OPENING_PREFERENCE) != null) {
                preopenPreference(extras.getString(EXTRA_OPENING_PREFERENCE));
            }
        }
    }

    private void preopenPreference(String key) {
        int position = findPreferencePosition(key);
        if (position >= 0) {
            getPreferenceScreen().onItemClick(null, null, position, 0);
        }
    }

    private int findPreferencePosition(String key) {
        ListAdapter listAdapter = getPreferenceScreen().getRootAdapter();
        for (int position = 0; position < listAdapter.getCount(); position++) {
            if (listAdapter.getItem(position).equals(findPreference(key))) {
                return position;
            }
        }
        return -1;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.endsWith("_active")) {
            key = "categories";
        }
        switch (key) {
            case "unit_bloodsugar":
                Events.post(new UnitChangedEvent(Measurement.Category.BLOODSUGAR));
                break;
            case "unit_meal":
                Events.post(new UnitChangedEvent(Measurement.Category.MEAL));
                break;
            case "unit_meal_factor":
                Events.post(new MealFactorUnitChangedEvent());
                break;
            case "unit_hba1c":
                Events.post(new UnitChangedEvent(Measurement.Category.HBA1C));
                break;
            case "unit_weight":
                Events.post(new UnitChangedEvent(Measurement.Category.WEIGHT));
                break;
        }
        setSummary(findPreference(key));
    }

    private void createBackup() {
        progressComponent.show(getActivity());
        Export.exportCsv(true, new FileListener() {

            @Override
            public void onProgress(String message) {
                progressComponent.setMessage(message);
            }

            @Override
            public void onSuccess(@Nullable File file, String mimeType) {
                progressComponent.dismiss();
                if (file != null) {
                    FileUtils.shareFile(getActivity(), file, mimeType);
                } else {
                    onError();
                }
            }

            @Override
            public void onError() {
                progressComponent.dismiss();
                Toast.makeText(getActivity(), getActivity().getString(R.string.error_unexpected), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void importBackup(Uri uri) {
        progressComponent.show(getActivity());
        Export.importCsv(getActivity(), uri, new FileListener() {

            @Override
            public void onProgress(String message) {
                progressComponent.setMessage(message);
            }

            @Override
            public void onSuccess(@Nullable File file, String mimeType) {
                progressComponent.dismiss();
                Toast.makeText(getActivity(), getActivity().getString(R.string.backup_complete), Toast.LENGTH_SHORT).show();
                Events.post(new BackupImportedEvent());
            }

            @Override
            public void onError() {
                progressComponent.dismiss();
                Toast.makeText(getActivity(), getActivity().getString(R.string.error_import), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FileProvidedEvent event) {
        importBackup(event.context);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FileProvidedFailedEvent event) {
        Toast.makeText(getActivity(), getActivity().getString(R.string.error_unexpected), Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PermissionResponseEvent event) {
        if (event.context == Permission.WRITE_EXTERNAL_STORAGE && event.isGranted) {
            switch (event.useCase) {
                case BACKUP_WRITE:
                    createBackup();
                    break;
                case BACKUP_READ:
                    FileUtils.searchFiles(getActivity(), Export.CSV_IMPORT_MIME_TYPE, BaseActivity.REQUEST_CODE_BACKUP_IMPORT);
                    break;
            }
        }
    }
}