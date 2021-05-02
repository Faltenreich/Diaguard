package com.faltenreich.diaguard.feature.timeline;

import androidx.annotation.StringRes;

import com.faltenreich.diaguard.R;

public enum  TimelineStyle {
    SCATTER_CHART(0, R.string.chart_scatter),
    LINE_CHART(1, R.string.chart_line);

    private final int stableId;
    private final int titleRes;

    TimelineStyle(int stableId, @StringRes int titleRes) {
        this.stableId = stableId;
        this.titleRes = titleRes;
    }

    public int getStableId() {
        return stableId;
    }

    @StringRes
    public int getTitleRes() {
        return titleRes;
    }
}
