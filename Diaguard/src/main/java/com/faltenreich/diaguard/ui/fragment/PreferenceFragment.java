package com.faltenreich.diaguard.ui.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.widget.ListAdapter;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.ui.activity.PreferenceActivity;
import com.faltenreich.diaguard.ui.view.preferences.BloodSugarPreference;
import com.faltenreich.diaguard.ui.view.preferences.CategoryPreference;
import com.faltenreich.diaguard.ui.view.preferences.CountryPreference;
import com.faltenreich.diaguard.util.Helper;
import com.faltenreich.diaguard.util.NumberUtils;

import java.util.ArrayList;

/**
 * Created by Faltenreich on 01.09.2016.
 */
public class PreferenceFragment extends android.preference.PreferenceFragment {

    public static final String EXTRA_OPENING_PREFERENCE = "EXTRA_OPENING_PREFERENCE";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sharedPreferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key.endsWith("_active")) {
                    key = "categories";
                }
                setSummary(findPreference(key));
            }
        });

        // Initialize summaries where making sense
        for (Preference preference : getPreferenceList(getPreferenceScreen(), new ArrayList<Preference>())) {
            setSummary(preference);
        }

        checkIntents();
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

    private void setSummary(Preference preference) {

        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            preference.setSummary(listPreference.getEntry());

        } else if (preference instanceof CategoryPreference) {
            int activeCategoriesCount = PreferenceHelper.getInstance().getActiveCategories().length;
            preference.setSummary(activeCategoriesCount + " " + getString(R.string.active));

        } else if (preference instanceof CountryPreference) {
            preference.setSummary(PreferenceHelper.getInstance().getCountryName());

        } else if (preference instanceof BloodSugarPreference) {
            String value = PreferenceHelper.getInstance().getValueForKey(preference.getKey());
            float number = NumberUtils.parseNumber(value);
            if (number > 0) {
                number = PreferenceHelper.getInstance().formatDefaultToCustomUnit(Measurement.Category.BLOODSUGAR, number);
                value = Helper.parseFloat(number);
                preference.setSummary(value + " " + PreferenceHelper.getInstance().getUnitAcronym(Measurement.Category.BLOODSUGAR));
            } else {
                preference.setSummary(null);
            }
        }
    }

    private void checkIntents() {
        if (getActivity() instanceof PreferenceActivity && getActivity().getIntent() != null && getActivity().getIntent().getExtras() != null) {
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
}