package com.faltenreich.diaguard;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.faltenreich.diaguard.preferences.BloodSugarPreference;

import java.util.Map;

/**
 * Created by Filip on 26.10.13.
 */
public class PreferenceActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(
                android.R.id.content,
                new PreferenceFragment()).commit();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        LinearLayout root = (LinearLayout)findViewById(android.R.id.list).getParent().getParent().getParent();
        View view = LayoutInflater.from(this).inflate(R.layout.toolbar, root, false);
        root.addView(view, 0);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle(getString(R.string.settings));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    public static class PreferenceFragment extends android.preference.PreferenceFragment {

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
            for(Map.Entry<String, ?> entry : sharedPreferences.getAll().entrySet()) {
                setSummary(entry.getKey());
            }

        }

        private void setSummary(String key) {
            Preference preference = findPreference(key);
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                preference.setSummary(listPreference.getEntry());
            }
            else if(preference instanceof BloodSugarPreference) {
                BloodSugarPreference bloodSugarPreference = (BloodSugarPreference) preference;
                // TODO: preference.setSummary(bloodSugarPreference.getValue());
            }
        }
    }
}