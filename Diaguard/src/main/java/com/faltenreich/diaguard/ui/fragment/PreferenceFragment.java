package com.faltenreich.diaguard.ui.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;

import com.faltenreich.diaguard.R;

import java.util.Map;

/**
 * Created by Faltenreich on 01.09.2016.
 */
public class PreferenceFragment extends android.preference.PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sharedPreferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                setSummary(key);
            }
        });

        // Initialize summaries where making sense
        for (Map.Entry<String, ?> entry : sharedPreferences.getAll().entrySet()) {
            setSummary(entry.getKey());
        }
    }

    private void setSummary(String key) {
        Preference preference = findPreference(key);
        // TODO: BloodSugarPreference
        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            preference.setSummary(listPreference.getEntry());
        }
    }
}