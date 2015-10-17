package com.faltenreich.diaguard.ui.view.chart;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Toast;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.entity.BloodSugar;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.util.ChartHelper;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.format.DateTimeFormat;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Filip on 07.07.2015.
 */
public class CategoryChart extends ScatterChart {

    private static final String TAG = CategoryChart.class.getSimpleName();

    private static final int LABELS_TO_SKIP = 2;

    private DateTime day;
    private Measurement.Category[] activeCategories;

    public CategoryChart(Context context) {
        super(context);
        this.day = DateTime.now();
        setup();
    }

    public CategoryChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.day = DateTime.now();
        setup();
    }

    private void setup() {
        if (!isInEditMode()) {
            Measurement.Category[] activeCategories = PreferenceHelper.getInstance().getActiveCategories();
            this.activeCategories = Arrays.copyOfRange(activeCategories, 1, activeCategories.length);

            ChartHelper.setChartDefaultStyle(this);
            getAxisLeft().setEnabled(false);
            getXAxis().setEnabled(false);

            new InitChartTask().execute();
        }
    }

    public DateTime getDay() {
        return day;
    }

    public void setDay(DateTime day) {
        this.day = day;
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
            setData(data);
            setVisibleXRangeMaximum(DateTimeConstants.MINUTES_PER_DAY);
            getXAxis().setLabelsToSkip((DateTimeConstants.MINUTES_PER_HOUR * LABELS_TO_SKIP) - 1);

            new UpdateChartDataTask().execute();
        }

        private List<String> getXLabels() {
            ArrayList<String> xLabels = new ArrayList<>();
            DateTime startTime = DateTime.now().withTimeAtStartOfDay();
            DateTime endTime = startTime.plusDays(1);

            while (startTime.isBefore(endTime)) {
                xLabels.add("");
                startTime = startTime.plusMinutes(1);
            }
            return xLabels;
        }

        private List<ScatterDataSet> getEmptyDataSets() {
            List<ScatterDataSet> dataSets = new ArrayList<>();
            for (Measurement.Category category : activeCategories) {
                if (category != Measurement.Category.BLOODSUGAR) {
                    dataSets.add(getDataSet(category));
                }
            }
            return dataSets;
        }

        private ScatterDataSet getDataSet(Measurement.Category category) {
            ScatterDataSet dataSet = new ScatterDataSet(new ArrayList<Entry>(), category.name());
            dataSet.setScatterShapeSize(ChartHelper.SCATTER_SIZE);
            dataSet.setScatterShape(ScatterShape.CIRCLE);
            dataSet.setDrawValues(true);
            return dataSet;
        }
    }

    private class UpdateChartDataTask extends AsyncTask<Void, Void, List<Measurement>> {

        protected List<Measurement> doInBackground(Void... params) {
            try {
                List<Measurement> measurements = new ArrayList<>();
                List<com.faltenreich.diaguard.data.entity.Entry> entries = EntryDao.getInstance().getEntriesOfDay(day);
                if (entries != null && entries.size() > 0) {
                    for (com.faltenreich.diaguard.data.entity.Entry entry : entries) {
                        List<Measurement> measurementsOfEntry = EntryDao.getInstance().getMeasurements(entry, activeCategories);
                        measurements.addAll(measurementsOfEntry);
                    }
                }
                return measurements;
            } catch (SQLException exception) {
                Log.e(TAG, exception.getMessage());
                return new ArrayList<>();
            }
        }

        protected void onPostExecute(List<Measurement> measurements) {
            try {
                // Remove old entries
                for (int position = 0; position < getData().getDataSetCount(); position++) {
                    getData().getDataSetByIndex(position).clear();
                }
                // Add new entries
                List<Measurement.Category> categories = Arrays.asList(activeCategories);
                for (Measurement measurement : measurements) {
                    com.faltenreich.diaguard.data.entity.Entry entry = measurement.getEntry();
                    int xValue = entry.getDate().getMinuteOfDay();
                    float yValue = categories.indexOf(measurement.getCategory());
                    Entry chartEntry = new Entry(PreferenceHelper.getInstance().formatDefaultToCustomUnit(Measurement.Category.BLOODSUGAR, yValue), xValue);
                    chartEntry.setData(measurement);
                    getData().getDataSetByLabel(measurement.getCategory().name(), true).addEntry(chartEntry);
                }
                notifyDataSetChanged();
                invalidate();
            } catch (Exception exception) {
                Log.e(TAG, exception.getMessage());
            }
        }
    }
}
