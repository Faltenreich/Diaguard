package com.faltenreich.diaguard.feature.navigation;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ToolbarManager {

    public static void applyToolbar(AppCompatActivity activity, Toolbar toolbar) {
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }
}
