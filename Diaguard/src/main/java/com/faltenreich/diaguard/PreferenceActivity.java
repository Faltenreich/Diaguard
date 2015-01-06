package com.faltenreich.diaguard;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by Filip on 26.10.13.
 */
public class PreferenceActivity extends android.preference.PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void setContentView(int layoutResID) {
        ViewGroup contentView = (ViewGroup) LayoutInflater.from(this).inflate(
                R.layout.activity_settings,
                new LinearLayout(this),
                false);

        Toolbar toolbar = (Toolbar) contentView.findViewById(R.id.toolbar);
        if(toolbar != null) {
            toolbar.setTitle(getString(R.string.settings));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        getWindow().setContentView(contentView);
    }

    @Override
    protected  boolean isValidFragment(String fragmentName) {
        // Needed to prevent RuntimeException on API >19
        return true;
    }
}