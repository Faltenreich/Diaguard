package com.faltenreich.diaguard.ui.view.chart;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.entity.BloodSugar;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.util.ChartHelper;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

/**
 * Created by Filip on 07.07.2015.
 */
public class DayChart extends CombinedChart {


    private static final int X_INDICES = DateTimeConstants.HOURS_PER_DAY / 2;
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

            int textColor = ContextCompat.getColor(getContext(), android.R.color.black);
            getAxisLeft().setTextColor(textColor);
            getXAxis().setTextColor(textColor);
            getXAxis().setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    int minute = (int) value;
                    int hour = minute / DateTimeConstants.MINUTES_PER_HOUR;
                    Log.d(DayChart.class.getSimpleName(), "Print label for minute " + minute + ": " + hour);
                    return Integer.toString(hour);
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

    private class FetchDataTask extends AsyncTask<Void, Void, DayChartData> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            clear();
        }

        @Nullable
        protected DayChartData doInBackground(Void... params) {
            List<BloodSugar> values = new ArrayList<>();
            List<Entry> entries = EntryDao.getInstance().getEntriesOfDay(day);
            if (entries != null && entries.size() > 0) {
                for (com.faltenreich.diaguard.data.entity.Entry entry : entries) {
                    List measurements = EntryDao.getInstance().getMeasurements(entry, new Measurement.Category[]{Measurement.Category.BLOODSUGAR});
                    values.addAll(measurements);
                }
            }
            return values.size() > 0 ? new DayChartData(getContext(), values) : null;
        }

        @Override
        protected void onPostExecute(@Nullable DayChartData data) {
            super.onPostExecute(data);
            if (data != null) {
                setData(data);
                float yAxisMaxValue = data.getYMax() > Y_MAX_VALUE ? data.getYMax() + Y_MAX_VALUE_OFFSET : Y_MAX_VALUE;
                getAxisLeft().setAxisMaximum(PreferenceHelper.getInstance().formatDefaultToCustomUnit(Measurement.Category.BLOODSUGAR, yAxisMaxValue));
            }
            invalidate();
        }
    }
}