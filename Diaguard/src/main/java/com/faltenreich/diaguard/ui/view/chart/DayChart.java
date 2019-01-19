package com.faltenreich.diaguard.ui.view.chart;

import android.content.Context;
import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import android.util.AttributeSet;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.ui.activity.EntryActivity;
import com.faltenreich.diaguard.util.ChartHelper;
import com.faltenreich.diaguard.util.thread.BaseAsyncTask;
import com.faltenreich.diaguard.util.thread.BloodSugarDayTask;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

/**
 * Created by Filip on 07.07.2015.
 */
public class DayChart extends CombinedChart implements OnChartValueSelectedListener {

    private static final float TAP_THRESHOLD_IN_DP = 24;
    private static final float Y_MAX_VALUE_DEFAULT = 200;
    private static final float Y_MAX_VALUE_OFFSET = 20;

    private DateTime day;

    public DayChart(Context context) {
        super(context);
        setup();
    }

    public DayChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    private void setup() {
        if (!isInEditMode()) {
            ChartHelper.setChartDefaultStyle(this, Measurement.Category.BLOODSUGAR);

            @ColorInt int textColor = ContextCompat.getColor(getContext(), android.R.color.black);
            getAxisLeft().setTextColor(textColor);
            getXAxis().setTextColor(textColor);
            getXAxis().setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
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

            // setMarker(new ChartMarkerView(getContext()));
            setMaxHighlightDistance(TAP_THRESHOLD_IN_DP);
            setOnChartValueSelectedListener(this);
            setDragEnabled(false);

            setDay(DateTime.now());
        }
    }

    private void update() {
        new BloodSugarDayTask(getContext(), new BaseAsyncTask.OnAsyncProgressListener<DayChartData>() {
            @Override
            public void onPostExecute(DayChartData data) {
                if (ViewCompat.isAttachedToWindow(DayChart.this)) {
                    clear();
                    setData(data);

                    // Identify max value manually because data.getYMax does not work when combining scatter with line chart
                    float yAxisMaximum = PreferenceHelper.getInstance().formatDefaultToCustomUnit(Measurement.Category.BLOODSUGAR, Y_MAX_VALUE_DEFAULT);
                    for (int datasetIndex = 0; datasetIndex < data.getScatterData().getDataSetCount(); datasetIndex++) {
                        IScatterDataSet dataSet = data.getScatterData().getDataSetByIndex(datasetIndex);
                        for (int entryIndex = 0; entryIndex < dataSet.getEntryCount(); entryIndex++) {
                            float entryValue = dataSet.getEntryForIndex(entryIndex).getY();
                            if (entryValue > yAxisMaximum) {
                                yAxisMaximum = entryValue;
                            }
                        }
                    }

                    getAxisLeft().setAxisMaximum(yAxisMaximum + Y_MAX_VALUE_OFFSET);
                    invalidate();
                }
            }
        }, day).execute();
    }

    public DateTime getDay() {
        return day;
    }

    public void setDay(DateTime day) {
        this.day = day;
        update();
    }

    @Override
    public void onValueSelected(com.github.mikephil.charting.data.Entry entry, Highlight highlight) {
        if (entry.getData() != null && entry.getData() instanceof Entry) {
            EntryActivity.show(getContext(), (Entry) entry.getData());
        }
    }

    @Override
    public void onNothingSelected() {

    }
}