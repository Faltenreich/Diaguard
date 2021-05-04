package com.faltenreich.diaguard.shared.view.chart;

import com.github.mikephil.charting.formatter.ValueFormatter;

/**
 * Created by Faltenreich on 01.04.2016.
 */
public class PercentValueFormatter extends ValueFormatter {

    @Override
    public String getFormattedValue(float value) {
        return String.format("%d%%", (int) value);
    }
}
