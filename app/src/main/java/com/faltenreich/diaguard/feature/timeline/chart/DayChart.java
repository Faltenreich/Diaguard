package com.faltenreich.diaguard.feature.timeline.chart;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.ColorInt;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Entry;
import com.faltenreich.diaguard.shared.view.chart.ChartUtils;
import com.faltenreich.diaguard.shared.view.listener.OnItemSelectedListener;
import com.faltenreich.diaguard.shared.view.resource.ColorUtils;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.joda.time.DateTimeConstants;

/**
 * Created by Filip on 07.07.2015.
 */
public class DayChart extends CombinedChart implements OnChartValueSelectedListener {

    private static final float TAP_THRESHOLD_IN_DP = 24;
    public static final float Y_MAX_VALUE_DEFAULT = 200;
    public static final float Y_MAX_VALUE_OFFSET = 20;

    private OnItemSelectedListener<Entry> listener;

    public DayChart(Context context) {
        super(context);
        setup();
    }

    public DayChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public void setOnItemSelectedListener(OnItemSelectedListener<Entry> listener) {
        this.listener = listener;
    }

    private void setup() {
        if (!isInEditMode()) {
            ChartUtils.setChartDefaultStyle(this, Category.BLOODSUGAR);

            @ColorInt int textColor = ColorUtils.getTextColorPrimary(getContext());
            @ColorInt int gridColor = ColorUtils.getBackgroundTertiary(getContext());
            getAxisLeft().setTextColor(textColor);
            getAxisLeft().setGridColor(gridColor);
            getAxisLeft().setGridLineWidth(1f);
            getXAxis().setGridLineWidth(1f);
            getXAxis().setGridColor(gridColor);
            getXAxis().setTextColor(textColor);
            getXAxis().setValueFormatter(new ValueFormatter() {
                @Override
                public String getAxisLabel(float value, AxisBase axis) {
                    int minute = (int) value;
                    int hour = minute / DateTimeConstants.MINUTES_PER_HOUR;
                    return hour < DateTimeConstants.HOURS_PER_DAY ? Integer.toString(hour) : "";
                }
            });
            getXAxis().setAxisMinimum(0);
            getXAxis().setAxisMaximum(DateTimeConstants.MINUTES_PER_DAY);
            getXAxis().setLabelCount((DateTimeConstants.HOURS_PER_DAY / 2) + 1, true);

            // Offsets are calculated manually
            float leftOffset = getContext().getResources().getDimension(R.dimen.chart_offset_left);
            float bottomOffset = getContext().getResources().getDimension(R.dimen.chart_offset_bottom);
            setViewPortOffsets(leftOffset, 0, 0, bottomOffset);

            setMaxHighlightDistance(TAP_THRESHOLD_IN_DP);
            setOnChartValueSelectedListener(this);
            setDragEnabled(false);
        }
    }

    @Override
    public void onValueSelected(com.github.mikephil.charting.data.Entry entry, Highlight highlight) {
        if (listener != null && entry.getData() != null && entry.getData() instanceof Entry) {
            listener.onItemSelected((Entry) entry.getData());
        }
    }

    @Override
    public void onNothingSelected() {

    }
}