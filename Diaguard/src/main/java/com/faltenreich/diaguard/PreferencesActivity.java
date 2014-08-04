package com.faltenreich.diaguard;

import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

/**
 * Created by Filip on 26.10.13.
 */
public class PreferencesActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(getString(R.string.settings));

        if(Build.VERSION.SDK_INT < 11)
            addPreferencesFromResource(R.xml.preferences);
        else
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new PreferenceFragmentMain())
                    .commit();
    }

    @Override
    protected  boolean isValidFragment(String fragmentName) {
        // Needed to prevent RuntimeException on API >19
        return true;
    }

    public static class PreferenceFragmentMain extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}