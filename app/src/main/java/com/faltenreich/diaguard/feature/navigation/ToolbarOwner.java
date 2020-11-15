package com.faltenreich.diaguard.feature.navigation;

import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.faltenreich.diaguard.shared.SystemUtils;

public interface ToolbarOwner {

    Toolbar getToolbar();

    TextView getTitleView();

    default void applyToolbar(AppCompatActivity activity) {
        activity.setSupportActionBar(getToolbar());
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        activity.setTitle(SystemUtils.getLabelForActivity(activity));
    }
}
