package com.faltenreich.diaguard;

import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Filip on 26.10.13.
 */
public class PreferenceActivity extends android.preference.PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < 11) {
            addPreferencesFromResource(R.xml.preferences);
        }
        else {
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

    public static class PreferenceFragment extends android.preference.PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}