package com.faltenreich.diaguard.ui.view;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.DatabaseFacade;
import com.faltenreich.diaguard.data.entity.BloodSugar;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.util.ChartHelper;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.LimitLine;
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
        setup();
    }

    public DayChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.day = DateTime.now();
        setup();
    }

    private void setup() {
        if (!isInEditMode()) {
            ChartHelper.setChartDefaultStyle(this);
            setOnChartValueSelectedListener(this);
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

    @Override
    public void onValueSelected(com.github.mikephil.charting.data.Entry e, int dataSetIndex, Highlight highlight) {
        ChartMarkerView markerView = new ChartMarkerView(getContext());
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

            new UpdateChartDataTask().execute();
        }

        private List<String> getXLabels() {
            ArrayList<String> xLabels = new ArrayList<>();
            DateTime startTime = DateTime.now().withTimeAtStartOfDay();
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
                dataSet.setScatterShapeSize(category == Measurement.Category.BLOODSUGAR ? ChartHelper.SCATTER_SIZE : ChartHelper.SCATTER_SIZE * 0.75f);
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

    private class UpdateChartDataTask extends AsyncTask<Void, Void, List<com.faltenreich.diaguard.data.entity.Entry>> {

        protected List<com.faltenreich.diaguard.data.entity.Entry> doInBackground(Void... params) {
            try {
                List<com.faltenreich.diaguard.data.entity.Entry> entries = DatabaseFacade.getInstance().getEntriesOfDay(day);
                if (entries != null && entries.size() > 0) {
                    for (com.faltenreich.diaguard.data.entity.Entry entry : entries) {
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

        protected void onPostExecute(List<com.faltenreich.diaguard.data.entity.Entry> entries) {
            try {
                // Remove old entries
                for (Measurement.Category category : PreferenceHelper.getInstance().getActiveCategories()) {
                    getData().getDataSetByLabel(category.name(), true).clear();
                }
                // Add new entries
                for (com.faltenreich.diaguard.data.entity.Entry entry : entries) {
                    for (Measurement measurement : entry.getMeasurementCache()) {
                        Measurement.Category category = measurement.getCategory();
                        int xValue = entry.getDate().getMinuteOfDay();
                        // TODO: Handle non-Bloodsugar values
                        float yValue = category == Measurement.Category.BLOODSUGAR ? ((BloodSugar) measurement).getMgDl() : 10;
                        getData().getDataSetByLabel(category.name(), true).addEntry(new Entry(yValue, xValue));
                    }
                }
                notifyDataSetChanged();
                invalidate();
            } catch (Exception exception) {
                Log.e(TAG, exception.getMessage());
            }
        }
    }
}
