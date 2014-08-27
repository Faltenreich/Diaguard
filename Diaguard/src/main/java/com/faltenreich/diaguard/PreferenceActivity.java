package com.faltenreich.diaguard;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

/**
 * Created by Filip on 26.10.13.
 */
public class PreferenceActivity extends android.preference.PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < 11)
            addPreferencesFromResource(R.xml.preferences);
        else {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new PreferenceFragment())
                    .commit();
        }
    }

    @Override
    protected  boolean isValidFragment(String fragmentName) {
        // Needed to prevent RuntimeException on API >19
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static class PreferenceFragment extends android.preference.PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}