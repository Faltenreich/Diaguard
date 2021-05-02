package com.faltenreich.diaguard.feature.navigation;

import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

public interface ToolbarOwner {
    Toolbar getToolbar();
    TextView getTitleView();
}
