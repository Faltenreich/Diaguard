package com.faltenreich.diaguard.ui.view;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.adapter.OnChartPageChangeListener;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.util.ArrayList;

/**
 * Created by Filip on 07.07.2015.
 */
public class EndlessChart extends ScatterChart implements OnChartGestureListener, OnChartPageChangeListener {

    private final int PAGE_COUNT = 3;
    private final int X_PER_PAGE = DateTimeConstants.MINUTES_PER_DAY;
    private final int X_TOTAL = X_PER_PAGE * PAGE_COUNT;

    private OnChartPageChangeListener chartPageChangeListener;

    private DateTime currentDateTime;
    private boolean isLoadingPage;

    public EndlessChart(Context context) {
        super(context);
    }

    public EndlessChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EndlessChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();
        setOnChartGestureListener(this);
        setChartPageChangeListener(this);
        currentDateTime = DateTime.now();
        updateChart();
    }

    private void updateChart() {
        new UpdateChartTask().execute(this);
    }

    private void updateData() {
        for(Measurement.Category category : PreferenceHelper.getInstance().getActiveCategories()) {
            // TODO
        }
    }

    public OnChartPageChangeListener getChartPageChangeListener() {
        return chartPageChangeListener;
    }

    public void setChartPageChangeListener(OnChartPageChangeListener chartPageChangeListener) {
        this.chartPageChangeListener = chartPageChangeListener;
    }

    @Override
    public void onChartPageChange(OnChartPageChangeListener.Direction direction) {
        isLoadingPage = true;
        currentDateTime = direction == Direction.LEFT ? currentDateTime.minusDays(1) : currentDateTime.plusDays(1);
        updateChart();
    }

    private boolean hasChangedPage(Direction direction) {
        float lowestVisibleXIndex = getLowestVisibleXIndex();
        switch (direction) {
            case LEFT:
                return lowestVisibleXIndex < X_PER_PAGE;
            case RIGHT:
                return lowestVisibleXIndex > X_PER_PAGE * 2;
            default:
                return false;
        }
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        if (!isLoadingPage) {
            Direction direction = dX > 0 ? Direction.LEFT : Direction.RIGHT;
            if (getChartPageChangeListener() != null && hasChangedPage(direction)) {
                float lowestVisibleXIndex = getLowestVisibleXIndex();
                getChartPageChangeListener().onChartPageChange(direction);
            }
        }
    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {
    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
    }

    private class UpdateChartTask extends AsyncTask<EndlessChart, Void, ScatterData> {

        private String[] weekDays = getResources().getStringArray(R.array.weekdays);
        private EndlessChart chart;

        protected ScatterData doInBackground(EndlessChart... params) {

            chart = params[0];

            // Set x-axis labels minute-precisely from previous to next day
            ArrayList<String> xLabels = new ArrayList<>();

            DateTime previousDay = currentDateTime
                    .minusDays(1)
                    .withTime(0, 0, 0, 0);
            DateTime nextDay = currentDateTime
                    .plusDays(1)
                    .withTime(DateTimeConstants.HOURS_PER_DAY - 1,
                            DateTimeConstants.MINUTES_PER_HOUR - 1,
                            DateTimeConstants.SECONDS_PER_MINUTE - 1,
                            DateTimeConstants.MILLIS_PER_SECOND - 1);

            while (previousDay.isBefore(nextDay)) {
                // Full hour
                if (previousDay.getMinuteOfHour() == 0) {
                    if (previousDay.getHourOfDay() == 0) {
                        String weekDay = weekDays[previousDay.dayOfWeek().get() - 1].substring(0, 2);
                        xLabels.add(previousDay.getHourOfDay() + "\n" +
                                weekDay + ", " + previousDay.getDayOfMonth() + "." + previousDay.getMonthOfYear());
                    } else {
                        xLabels.add(Integer.toString(previousDay.getHourOfDay()));
                    }
                } else {
                    xLabels.add("");
                }
                previousDay = previousDay.plusMinutes(1);
            }

            return new ScatterData(xLabels, new ArrayList<ScatterDataSet>());
        }

        protected void onProgressUpdate(Void... progress) {
        }

        protected void onPostExecute(ScatterData data) {
            chart.setData(data);
            chart.setVisibleXRangeMaximum(DateTimeConstants.MINUTES_PER_DAY);
            chart.getXAxis().setLabelsToSkip((DateTimeConstants.MINUTES_PER_HOUR * 4) - 1);
            chart.moveViewToX(X_PER_PAGE);
            chart.invalidate();
            updateData();
            isLoadingPage = false;
        }
    }
}
