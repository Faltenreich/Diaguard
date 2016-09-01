package com.faltenreich.diaguard.ui.activity;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.ui.view.preferences.BloodSugarPreference;
import com.faltenreich.diaguard.util.SystemUtils;
import com.faltenreich.diaguard.util.event.Events;
import com.faltenreich.diaguard.util.event.PermissionDeniedEvent;
import com.faltenreich.diaguard.util.event.PermissionGrantedEvent;

import java.util.Map;

/**
 * Created by Filip on 26.10.13.
 */
public class PreferenceActivity extends BaseActivity {
    
    public PreferenceActivity() {
        super(R.layout.activity_preferences);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(
                R.id.content,
                new PreferenceFragment()).commit();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case SystemUtils.PERMISSION_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Events.post(new PermissionGrantedEvent(Manifest.permission.WRITE_EXTERNAL_STORAGE));
                } else {
                    Events.post(new PermissionDeniedEvent(Manifest.permission.WRITE_EXTERNAL_STORAGE));
                }
            }
        }
    }
}