package com.faltenreich.diaguard.ui.chart;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;

import com.faltenreich.diaguard.database.DatabaseFacade;
import com.faltenreich.diaguard.database.measurements.BloodSugar;
import com.faltenreich.diaguard.database.measurements.Measurement;
import com.faltenreich.diaguard.helpers.PreferenceHelper;
import com.github.mikephil.charting.charts.ScatterChart;
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
        //new UpdateChartDataTask().execute();
    }

    private class InitChartTask extends AsyncTask<Void, Void, ScatterData> {

        protected ScatterData doInBackground(Void... params) {
            // Set x-axis labels minute-precisely
            ArrayList<String> xLabels = new ArrayList<>();
            DateTime startTime = currentDay.withTime(0, 0, 0, 0);
            DateTime endTime = startTime.plusDays(1);

            while (startTime.isBefore(endTime)) {
                // Full hour
                if (startTime.getMinuteOfHour() == 0) {
                    xLabels.add(Integer.toString(startTime.getHourOfDay()));
                } else {
                    xLabels.add("");
                }
                startTime = startTime.plusMinutes(1);
            }

            List<ScatterDataSet> dataSets = new ArrayList<>();
            for(Measurement.Category category : PreferenceHelper.getInstance().getActiveCategories()) {
                dataSets.add(new ScatterDataSet(new ArrayList<Entry>(), category.name()));
            }

            return new ScatterData(xLabels, dataSets);
        }

        protected void onProgressUpdate(Void... progress) {
        }

        protected void onPostExecute(ScatterData data) {
            setData(data);
            setVisibleXRangeMaximum(DateTimeConstants.MINUTES_PER_DAY);
            getXAxis().setLabelsToSkip((DateTimeConstants.MINUTES_PER_HOUR * LABELS_TO_SKIP) - 1);
            invalidate();
            setData(currentDay);
        }
    }

    private class UpdateChartDataTask extends AsyncTask<Void, Void, HashMap<Measurement.Category, ScatterDataSet>> {

        protected HashMap<Measurement.Category, ScatterDataSet> doInBackground(Void... params) {
            try {
                HashMap<Measurement.Category, ScatterDataSet> dataSets = new HashMap<>();
                for(Measurement.Category category : PreferenceHelper.getInstance().getActiveCategories()) {
                    dataSets.put(category, new ScatterDataSet(new ArrayList<Entry>(), category.name()));
                }

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
                                dataSets.get(category).addEntry(new Entry(yValue, xValue));
                            }
                        }
                    }
                }
                return dataSets;
            } catch (SQLException exception) {
                Log.e(TAG, exception.getMessage());
                return null;
            }
        }

        protected void onProgressUpdate(Void... progress) {
        }

        protected void onPostExecute(HashMap<Measurement.Category, ScatterDataSet> dataSets) {
            for (Map.Entry<Measurement.Category, ScatterDataSet> entry : dataSets.entrySet()) {
                getData().getDataSetByLabel(entry.getKey().name(), true).clear();
                ScatterDataSet scatterDataSet = entry.getValue();
                if (scatterDataSet != null && scatterDataSet.getYVals().size() > 0) {
                    getData().addDataSet(entry.getValue());
                }
            }
            invalidate();
        }
    }
}
