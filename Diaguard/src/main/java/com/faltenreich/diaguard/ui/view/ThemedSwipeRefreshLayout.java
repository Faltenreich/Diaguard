package com.faltenreich.diaguard.ui.view;

import android.content.Context;
import android.util.AttributeSet;

import com.faltenreich.diaguard.util.ResourceUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class ThemedSwipeRefreshLayout extends SwipeRefreshLayout {

    public ThemedSwipeRefreshLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public ThemedSwipeRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setProgressBackgroundColorSchemeColor(ResourceUtils.getBackgroundSecondary(getContext()));
    }
}
