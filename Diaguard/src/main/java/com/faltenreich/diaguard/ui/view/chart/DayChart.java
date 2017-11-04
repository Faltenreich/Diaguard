package com.faltenreich.diaguard.ui.view.chart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.ui.activity.EntryActivity;
import com.faltenreich.diaguard.util.ChartHelper;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Filip on 07.07.2015.
 */
public class DayChart extends CombinedChart implements OnChartValueSelectedListener {

    private static final float Y_MAX_VALUE = 275;
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
            float leftOffset = getContext().getResources().getDimension(R.dimen.size_image) +
                    getContext().getResources().getDimension(R.dimen.margin_between);
            float bottomOffset = getContext().getResources().getDimension(R.dimen.chart_offset_bottom);
            setViewPortOffsets(leftOffset, 0, 0, bottomOffset);

            // setMarker(new ChartMarkerView(getContext()));
            setOnChartValueSelectedListener(this);

            setDay(DateTime.now());
        }
    }

    public DateTime getDay() {
        return day;
    }

    public void setDay(DateTime day) {
        this.day = day;
        new FetchDataTask().execute();
    }

    @Override
    public void onValueSelected(com.github.mikephil.charting.data.Entry entry, Highlight highlight) {
        if (entry.getData() != null && entry.getData() instanceof Entry) {
            Intent intent = new Intent(getContext(), EntryActivity.class);
            intent.putExtra(EntryActivity.EXTRA_ENTRY, ((Entry) entry.getData()).getId());
            getContext().startActivity(intent);
        }
    }

    @Override
    public void onNothingSelected() {

    }

    @SuppressLint("StaticFieldLeak")
    private class FetchDataTask extends AsyncTask<Void, Void, DayChartData> {

        protected DayChartData doInBackground(Void... params) {
            List<Measurement> values = new ArrayList<>();
            List<Entry> entries = EntryDao.getInstance().getEntriesOfDay(day);
            if (entries != null && entries.size() > 0) {
                for (com.faltenreich.diaguard.data.entity.Entry entry : entries) {
                    // TODO: Improve performance by using transaction / bulk fetch
                    List<Measurement> measurements = EntryDao.getInstance().getMeasurements(entry, new Measurement.Category[] { Measurement.Category.BLOODSUGAR });
                    values.addAll(measurements);
                }
            }
            return new DayChartData(getContext(), values);
        }

        @Override
        protected void onPostExecute(DayChartData data) {
            super.onPostExecute(data);

            clear();
            setData(data);

            float yAxisMaximum = PreferenceHelper.getInstance().formatCustomToDefaultUnit(Measurement.Category.BLOODSUGAR, data.getYMax());
            yAxisMaximum = yAxisMaximum > Y_MAX_VALUE ? yAxisMaximum + Y_MAX_VALUE_OFFSET : Y_MAX_VALUE;
            yAxisMaximum = PreferenceHelper.getInstance().formatDefaultToCustomUnit(Measurement.Category.BLOODSUGAR, yAxisMaximum);
            getAxisLeft().setAxisMaximum(yAxisMaximum);

            invalidate();
        }
    }
}