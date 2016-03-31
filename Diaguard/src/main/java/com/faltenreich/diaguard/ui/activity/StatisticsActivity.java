package com.faltenreich.diaguard.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.dao.MeasurementDao;
import com.faltenreich.diaguard.data.entity.BloodSugar;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.util.ChartHelper;
import com.faltenreich.diaguard.util.TimeSpan;
import com.faltenreich.diaguard.util.thread.BaseAsyncTask;
import com.faltenreich.diaguard.util.thread.UpdateChartTask;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.data.LineData;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;

import butterknife.Bind;

/**
 * Created by Filip on 15.11.13.
 */

public class StatisticsActivity extends BaseActivity {

    @Bind(R.id.statistics_period)
    protected TextView textViewPeriod;

    @Bind(R.id.statistics_measurement_count)
    protected TextView textViewMeasurementCount;

    @Bind(R.id.statistics_measurement_count_avg)
    protected TextView textViewMeasurementCountAvg;

    @Bind(R.id.statistics_avg_unit)
    protected TextView textViewAvgUnit;

    @Bind(R.id.statistics_avg_value)
    protected TextView textViewAvgValue;

    @Bind(R.id.statistics_chart_trend)
    protected LineChart chart;

    private TimeSpan timeSpan;
    private Measurement.Category category;

    public StatisticsActivity() {
        super(R.layout.activity_statistics);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateContent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.statistics, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (timeSpan != null) {
            MenuItem timeIntervalMenuItem = menu.findItem(R.id.action_time_interval);
            if (timeIntervalMenuItem != null) {
                timeIntervalMenuItem.setTitle(timeSpan.toLocalizedString());
                timeIntervalMenuItem.setIcon(timeSpan.getImageResId());
            }
        }
        if (category != null) {
            MenuItem categoryMenuItem = menu.findItem(R.id.action_category);
            if (categoryMenuItem != null) {
                categoryMenuItem.setIcon(PreferenceHelper.getInstance().getCategoryImageResourceId(category));
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_time_interval:
                skipTimeInterval(item);
                return true;
            case R.id.action_category:
                showCategoriesDialog(item);
                return true;
        }
        return false;
    }

    private void init() {
        this.timeSpan = TimeSpan.WEEK;
        this.category = Measurement.Category.BLOODSUGAR;
        initLayout();
    }

    private void initLayout() {
        initializeChart();
    }

    private void updateContent() {
        updateAverage();
        updateChart();
    }

    private void skipTimeInterval(MenuItem item) {
        int nextOrdinal = timeSpan.ordinal() + 1;
        this.timeSpan = TimeSpan.values()[nextOrdinal < TimeSpan.values().length ? nextOrdinal : 0];
        item.setTitle(timeSpan.toLocalizedString());
        item.setIcon(timeSpan.getImageResId());
        updateContent();
    }

    private void showCategoriesDialog(final MenuItem menuItem) {
        final Measurement.Category[] categories = PreferenceHelper.getInstance().getActiveCategories();
        String[] categoryNames = new String[categories.length];
        for (int position = 0; position < categories.length; position++) {
            categoryNames[position] = categories[position].toLocalizedString();
        }
        new AlertDialog.Builder(this)
                .setTitle(R.string.categories)
                .setItems(categoryNames,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                changeCategory(menuItem, categories[which]);
                            }
                        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create()
                .show();
    }

    private void changeCategory(MenuItem menuItem, Measurement.Category category) {
        this.category = category;
        menuItem.setIcon(PreferenceHelper.getInstance().getCategoryImageResourceId(category));
        updateContent();
    }

    private void updateAverage() {
        Interval interval = timeSpan.getPastInterval(DateTime.now());

        textViewPeriod.setText(String.format("%s\n- %s",
                DateTimeFormat.mediumDate().print(interval.getStart()),
                DateTimeFormat.mediumDate().print(interval.getEnd())));

        long count = EntryDao.getInstance().count(category, interval.getStart(), interval.getEnd());
        textViewMeasurementCount.setText(String.format("%d", count));

        long days = interval.toDuration().getStandardDays();
        float avgCountPerDay = (float) count / (float) days;
        textViewMeasurementCountAvg.setText(String.format("%.2f", avgCountPerDay));

        textViewAvgUnit.setText(String.format("%s %s",
                getString(R.string.average_symbol),
                PreferenceHelper.getInstance().getUnitAcronym(category)));

        float avgValue = MeasurementDao.getInstance(BloodSugar.class).avg(BloodSugar.Column.MGDL, interval);
        float avgValueCustom = PreferenceHelper.getInstance().formatDefaultToCustomUnit(Measurement.Category.BLOODSUGAR, avgValue);
        textViewAvgValue.setText(PreferenceHelper.getInstance().getDecimalFormat(Measurement.Category.BLOODSUGAR).format(avgValueCustom));
    }

    // region Charting

    private void initializeChart() {
        ChartHelper.setChartDefaultStyle(chart);
        chart.setTouchEnabled(false);
        chart.getAxisLeft().setDrawAxisLine(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisLeft().setDrawLabels(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.getXAxis().setTextColor(ContextCompat.getColor(this, R.color.gray_dark));
        chart.getXAxis().setLabelsToSkip(0);
        chart.getAxisLeft().addLimitLine(getLimitLine());
    }

    private LimitLine getLimitLine() {
        float targetValue = PreferenceHelper.getInstance().
                formatDefaultToCustomUnit(Measurement.Category.BLOODSUGAR,
                        PreferenceHelper.getInstance().getTargetValue());
        LimitLine limitLine = new LimitLine(targetValue, getString(R.string.hyper));
        limitLine.setLineColor(ContextCompat.getColor(this, R.color.gray_light));
        limitLine.setLabel(null);
        return limitLine;
    }


    private void updateChart() {
        new UpdateChartTask(this, new BaseAsyncTask.OnAsyncProgressListener<LineData>() {
            @Override
            public void onPostExecute(LineData lineData) {
                chart.setData(lineData);
                chart.invalidate();
            }
        }, category, timeSpan).execute();
    }

    // endregion
}