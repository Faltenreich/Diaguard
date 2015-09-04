package com.faltenreich.diaguard.adapter;

/**
 * Created by Filip on 07.07.2015.
 */
public interface OnChartPageChangeListener {
    enum Direction {
        LEFT,
        RIGHT
    }
    void onChartPageChange(Direction direction);
}
