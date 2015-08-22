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
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Filip on 07.07.2015.
 */
public class DayChart extends ScatterChart implements OnChartValueSelectedListener {

    private static final String TAG = DayChart.class.getSimpleName();

    private static final int LABELS_TO_SKIP = 2;

    private DateTime day;

    public DayChart(Context context) {
        super(context);
        this.day = DateTime.now();
    }

    public DayChart(Context context, DateTime day) {
        super(context);
        this.day = day;
    }

    public DayChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.day = DateTime.now();
    }

    public void setup() {
        ChartHelper.setChartDefaultStyle(this);
        setOnChartValueSelectedListener(this);
        new InitChartTask().execute();
    }

    public void setDateTime(DateTime day) {
        this.day = day;
        new UpdateChartDataTask().execute();
    }

    @Override
    public void onValueSelected(com.github.mikephil.charting.data.Entry e, int dataSetIndex, Highlight h) {
        MarkerView markerView = new ChartMarkerView(getContext());
        // TODO
        setMarkerView(markerView);
    }

    @Override
    public void onNothingSelected() {
        // TODO: Dismiss MarkerView
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
            setVisibleXRangeMaximum(DateTimeConstants.MINUTES_PER_DAY);
            getXAxis().setLabelsToSkip((DateTimeConstants.MINUTES_PER_HOUR * LABELS_TO_SKIP) - 1);

            setDateTime(day);
        }

        private List<String> getXLabels() {
            ArrayList<String> xLabels = new ArrayList<>();
            DateTime startTime = day.withTime(0, 0, 0, 0);
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
                ScatterDataSet dataSet = new ScatterDataSet(new ArrayList<Entry>(), category.name());
                int dataSetColor = getResources().getColor(PreferenceHelper.getInstance().getCategoryColorResourceId(category));
                dataSet.setColor(dataSetColor);
                dataSet.setScatterShapeSize(ChartHelper.SCATTER_SIZE);
                dataSet.setScatterShape(ScatterShape.CIRCLE);
                dataSet.setDrawValues(false);
                dataSets.add(dataSet);
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

    private class UpdateChartDataTask extends AsyncTask<Void, Void, List<com.faltenreich.diaguard.database.Entry>> {

        protected List<com.faltenreich.diaguard.database.Entry> doInBackground(Void... params) {
            try {
                List<com.faltenreich.diaguard.database.Entry> entries = DatabaseFacade.getInstance().getEntriesOfDay(day);
                if (entries != null && entries.size() > 0) {
                    for (com.faltenreich.diaguard.database.Entry entry : entries) {
                        for (Measurement.Category category : PreferenceHelper.getInstance().getActiveCategories()) {
                            Measurement measurement = DatabaseFacade.getInstance().getMeasurement(entry, category);
                            if (measurement != null) {
                                entry.getMeasurementCache().add(measurement);
                            }
                        }
                    }
                }
                return entries;
            } catch (SQLException exception) {
                Log.e(TAG, exception.getMessage());
            }
            return null;
        }

        protected void onProgressUpdate(Void... progress) {
        }

        protected void onPostExecute(List<com.faltenreich.diaguard.database.Entry> entries) {
            for (com.faltenreich.diaguard.database.Entry entry : entries) {
                for (Measurement measurement : entry.getMeasurementCache()) {
                    Measurement.Category category = measurement.getMeasurementType();
                    int xValue = entry.getDate().getHourOfDay() * entry.getDate().getMinuteOfHour();
                    // TODO: Handle non-Bloodsugar values
                    float yValue = category == Measurement.Category.BloodSugar ? ((BloodSugar) measurement).getMgDl() : 500;
                    getData().getDataSetByLabel(category.name(), true).addEntry(new Entry(yValue, xValue));
                }
            }
            notifyDataSetChanged();
            invalidate();
        }
    }
}
