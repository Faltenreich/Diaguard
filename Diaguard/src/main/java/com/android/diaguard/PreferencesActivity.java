package com.android.diaguard;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import java.util.List;

/**
 * Created by Filip on 26.10.13.
 */
public class PreferencesActivity extends PreferenceActivity {

    @Override
    public void onBuildHeaders(List<Header> target) {
        super.onBuildHeaders(target);
        loadHeadersFromResource(R.xml.headers, target);
    }

    @Override
    protected  boolean isValidFragment(String fragmentName) {
        // Needed to prevent RuntimeException on API >19
        return true;
    }

    public static class PreferenceFragmentApplication extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences_application);
        }
    }

    public static class PreferenceFragmentTherapy extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences_therapy);
        }
    }
}