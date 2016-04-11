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
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Filip on 07.07.2015.
 */
public class DayChart extends ScatterChart implements OnChartValueSelectedListener {

    private static final String TAG = DayChart.class.getSimpleName();

    private static final String DATA_SET_BLOODSUGAR = "bloodSugar";
    private static final String DATA_SET_BLOODSUGAR_HYPERGLYCEMIA = "hyperglycemia";
    private static final String DATA_SET_BLOODSUGAR_HYPOGLYCEMIA = "hypoglycemia";

    private static final int LABELS_TO_SKIP = 2;
    private static final float Y_MAX_VALUE = 275;
    private static final float Y_MAX_VALUE_OFFSET = 20;

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
            ChartHelper.setChartDefaultStyle(this, Measurement.Category.BLOODSUGAR);
            int textColor = ContextCompat.getColor(getContext(), android.R.color.black);
            getAxisLeft().setTextColor(textColor);
            getXAxis().setTextColor(textColor);

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
        Toast.makeText(getContext(), "Click", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected() {
        // TODO: Dismiss MarkerView
    }

    private class InitChartTask extends AsyncTask<Void, Void, ScatterData> {

        protected ScatterData doInBackground(Void... params) {
            List<String> xLabels = getXLabels();
            List<IScatterDataSet> dataSets = getEmptyDataSets();
            return new ScatterData(xLabels, dataSets);
        }

        protected void onProgressUpdate(Void... progress) {
        }

        protected void onPostExecute(ScatterData data) {
            setData(data);
            setVisibleXRangeMaximum(DateTimeConstants.MINUTES_PER_DAY);
            getXAxis().setLabelsToSkip((DateTimeConstants.MINUTES_PER_HOUR * LABELS_TO_SKIP) - 1);

            // Offsets are calculated manually (possible leak)
            float leftOffset = getContext().getResources().getDimension(R.dimen.size_image) +
                    getContext().getResources().getDimension(R.dimen.margin_between);
            float bottomOffset = getContext().getResources().getDimension(R.dimen.chart_offset_bottom);
            setViewPortOffsets(leftOffset, 0, 0, bottomOffset);

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

        private List<IScatterDataSet> getEmptyDataSets() {
            List<IScatterDataSet> dataSets = new ArrayList<>();
            dataSets.add(getDataSet(DATA_SET_BLOODSUGAR, R.color.green));
            if (PreferenceHelper.getInstance().limitsAreHighlighted()) {
                dataSets.add(getDataSet(DATA_SET_BLOODSUGAR_HYPERGLYCEMIA, R.color.red));
                dataSets.add(getDataSet(DATA_SET_BLOODSUGAR_HYPOGLYCEMIA, R.color.blue));
            }
            return dataSets;
        }

        private ScatterDataSet getDataSet(String title, @ColorRes int colorResourceId) {
            ScatterDataSet dataSet = new ScatterDataSet(new ArrayList<Entry>(), title);
            int dataSetColor = ContextCompat.getColor(getContext(), colorResourceId);
            dataSet.setColor(dataSetColor);
            dataSet.setScatterShapeSize(ChartHelper.SCATTER_SIZE);
            dataSet.setScatterShape(ScatterShape.CIRCLE);
            dataSet.setDrawValues(false);
            return dataSet;
        }
    }

    private class UpdateChartDataTask extends AsyncTask<Void, Void, List<BloodSugar>> {

        protected List<BloodSugar> doInBackground(Void... params) {
            List<BloodSugar> bloodSugarList = new ArrayList<>();
            List<com.faltenreich.diaguard.data.entity.Entry> entries = EntryDao.getInstance().getEntriesOfDay(day);
            if (entries != null && entries.size() > 0) {
                for (com.faltenreich.diaguard.data.entity.Entry entry : entries) {
                    List measurements = EntryDao.getInstance().getMeasurements(entry, new Measurement.Category[] {Measurement.Category.BLOODSUGAR});
                    bloodSugarList.addAll(measurements);
                }
            }
            return bloodSugarList;
        }

        protected void onPostExecute(List<BloodSugar> bloodSugarList) {
            try {
                // Remove old entries
                for (int position = 0; position < getData().getDataSetCount(); position++) {
                    getData().getDataSetByIndex(position).clear();
                }
                // Add new entries
                float maxValue = 0f;
                for (BloodSugar bloodSugar : bloodSugarList) {
                    com.faltenreich.diaguard.data.entity.Entry entry = bloodSugar.getEntry();
                    int xValue = entry.getDate().getMinuteOfDay();
                    float yValue = bloodSugar.getMgDl();
                    Entry chartEntry = new Entry(PreferenceHelper.getInstance().formatDefaultToCustomUnit(Measurement.Category.BLOODSUGAR, yValue), xValue);
                    if (PreferenceHelper.getInstance().limitsAreHighlighted()) {
                        if (yValue > PreferenceHelper.getInstance().getLimitHyperglycemia()) {
                            getData().getDataSetByLabel(DATA_SET_BLOODSUGAR_HYPERGLYCEMIA, true).addEntry(chartEntry);
                        } else if (yValue < PreferenceHelper.getInstance().getLimitHypoglycemia()) {
                            getData().getDataSetByLabel(DATA_SET_BLOODSUGAR_HYPOGLYCEMIA, true).addEntry(chartEntry);
                        } else {
                            getData().getDataSetByLabel(DATA_SET_BLOODSUGAR, true).addEntry(chartEntry);
                        }
                    } else {
                        getData().getDataSetByLabel(DATA_SET_BLOODSUGAR, true).addEntry(chartEntry);
                    }
                    if (yValue > maxValue) {
                        maxValue = yValue;
                    }
                }
                float yAxisMaxValue = maxValue > Y_MAX_VALUE ? maxValue + Y_MAX_VALUE_OFFSET : Y_MAX_VALUE;
                getAxisLeft().setAxisMaxValue(PreferenceHelper.getInstance().formatDefaultToCustomUnit(Measurement.Category.BLOODSUGAR, yAxisMaxValue));
                notifyDataSetChanged();
                invalidate();
            } catch (Exception exception) {
                Log.e(TAG, exception.getMessage());
            }
        }
    }
}
