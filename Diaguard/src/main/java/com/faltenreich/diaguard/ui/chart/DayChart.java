package com.faltenreich.diaguard.ui.chart;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.database.DatabaseFacade;
import com.faltenreich.diaguard.database.measurements.BloodSugar;
import com.faltenreich.diaguard.database.measurements.Measurement;
import com.faltenreich.diaguard.helpers.PreferenceHelper;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Filip on 07.07.2015.
 */
public class DayChart extends ScatterChart {

    private static final String TAG = DayChart.class.getSimpleName();

    private static final int LABELS_TO_SKIP = 2;

    private DateTime currentDay;

    public DayChart(Context context) {
        super(context);
    }

    public DayChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DayChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();
        currentDay = DateTime.now();
        initChart();
    }

    private void initChart() {
        new InitChartTask().execute();
    }

    public void setData(DateTime day) {
        currentDay = day;
        new UpdateChartDataTask().execute();
    }

    private class InitChartTask extends AsyncTask<Void, Void, ScatterData> {

        protected ScatterData doInBackground(Void... params) {
            List<String> xLabels = getXLabels();
            List<ScatterDataSet> dataSets = getEmptyDataSets();
            return new ScatterData(xLabels, dataSets);
        }

        protected void onProgressUpdate(Void... progress) {
        }

        protected void onPostExecute(ScatterData data) {
            if (PreferenceHelper.getInstance().limitsAreHighlighted()) {
                addLimitLines();
            }

            setData(data);
            setVisibleYRangeMaximum(300, YAxis.AxisDependency.LEFT);
            setVisibleXRangeMaximum(DateTimeConstants.MINUTES_PER_DAY);
            getXAxis().setLabelsToSkip((DateTimeConstants.MINUTES_PER_HOUR * LABELS_TO_SKIP) - 1);
            invalidate();

            setData(currentDay);
        }

        private List<String> getXLabels() {
            ArrayList<String> xLabels = new ArrayList<>();
            DateTime startTime = currentDay.withTime(0, 0, 0, 0);
            DateTime endTime = startTime.plusDays(1);

            while (startTime.isBefore(endTime)) {
                // Add label for full hour
                xLabels.add(startTime.getMinuteOfHour() == 0 ?
                        Integer.toString(startTime.getHourOfDay()) :
                        "");
                startTime = startTime.plusMinutes(1);
            }
            return xLabels;
        }

        private List<ScatterDataSet> getEmptyDataSets() {
            List<ScatterDataSet> dataSets = new ArrayList<>();
            for(Measurement.Category category : PreferenceHelper.getInstance().getActiveCategories()) {
                dataSets.add(new ScatterDataSet(new ArrayList<Entry>(), category.name()));
            }
            return dataSets;
        }

        private void addLimitLines() {
            LimitLine hyperglycemia = new LimitLine(
                    PreferenceHelper.getInstance().getLimitHyperglycemia(),
                    getContext().getString(R.string.hyper));
            hyperglycemia.setLineColor(getResources().getColor(R.color.red));
            hyperglycemia.setLabel(null);
            getAxisLeft().addLimitLine(hyperglycemia);

            LimitLine hypoglycemia = new LimitLine(
                    PreferenceHelper.getInstance().getLimitHypoglycemia(),
                    getContext().getString(R.string.hypo));
            hypoglycemia.setLineColor(getResources().getColor(R.color.blue));
            hypoglycemia.setLabel(null);
            getAxisLeft().addLimitLine(hypoglycemia);
        }
    }

    private class UpdateChartDataTask extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void... params) {
            try {
                List<com.faltenreich.diaguard.database.Entry> entries = DatabaseFacade.getInstance().getEntriesOfDay(currentDay);
                if (entries != null && entries.size() > 0) {
                    for (com.faltenreich.diaguard.database.Entry entry : entries) {
                        for (Measurement.Category category : PreferenceHelper.getInstance().getActiveCategories()) {
                            Measurement measurement = (DatabaseFacade.getInstance().getMeasurement(entry, category));
                            if (measurement != null) {
                                int xValue = entry.getDate().getHourOfDay() * entry.getDate().getMinuteOfHour();
                                float yValue = category == Measurement.Category.BloodSugar ?
                                        ((BloodSugar) measurement).getMgDl() :
                                        0;
                                getData().getDataSetByLabel(category.name(), true).addEntry(new Entry(yValue, xValue));
                            }
                        }
                    }
                }
            } catch (SQLException exception) {
                Log.e(TAG, exception.getMessage());
            }
            return null;
        }

        protected void onProgressUpdate(Void... progress) {
        }

        protected void onPostExecute(Void param) {
            notifyDataSetChanged();
            invalidate();
        }
    }
}
