package com.android.diaguard;

import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.view.MenuItem;

/**
 * Created by Filip on 26.10.13.
 */
public class PreferencesActivity extends PreferenceActivity {

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

    /**
     * This fragment contains a second-level set of preference that you
     * can get to by tapping an item in the first settings fragment.
     */
    public static class PreferenceFragmentMain extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}