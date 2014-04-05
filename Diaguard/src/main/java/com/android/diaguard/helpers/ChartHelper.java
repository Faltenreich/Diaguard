package com.android.diaguard.helpers;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;

import com.android.diaguard.R;
import com.android.diaguard.database.Event;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

/**
 * Created by Filip on 01.12.13.
 */
public class ChartHelper {

    Activity activity;

    /** The main dataset that includes all the series that go into a chart. */
    public XYMultipleSeriesDataset seriesDataset;
    /** The main rendererBloodSugar that includes all the renderers customizing a chart. */
    public XYMultipleSeriesRenderer renderer;
    /** The chart view that displays the data. */
    public GraphicalView chartView;

    public ChartHelper(Activity activity) {
        seriesDataset = new XYMultipleSeriesDataset();
        renderer = new XYMultipleSeriesRenderer();
        this.activity = activity;
    }

    public void initialize() {

        // Needed for empty chart to render correctly
        XYSeriesRenderer rendererFake = new XYSeriesRenderer();
        renderer.addSeriesRenderer(rendererFake);
        XYSeries seriesFake = new XYSeries("");
        seriesDataset.addSeries(seriesFake);
        seriesFake.add(-999, -999);

        chartView = ChartFactory.getLineChartView(activity, seriesDataset, renderer);
    }

    public void render() {
        renderBasics();
        renderInApp();
    }

    private void renderBasics() {
        renderer.removeAllRenderers();

        renderer.setClickEnabled(true);
        renderer.setSelectableBuffer(10);
        renderer.setAntialiasing(true);

        renderer.setPanEnabled(false, false);
        renderer.setZoomEnabled(false, false);

        renderer.setMargins(new int[]{0, (int) Helper.getDPI(activity, 40), 0, (int) Helper.getDPI(activity, 10)});
        renderer.setMarginsColor(activity.getResources().getColor(R.color.ltgray));
        renderer.setLabelsTextSize(Helper.getDPI(activity, 12));
        renderer.setLabelsColor(Color.BLACK);
        renderer.setPointSize(Helper.getDPI(activity, 4));

        renderer.setApplyBackgroundColor(true);
        renderer.setBackgroundColor(activity.getResources().getColor(R.color.ltgray));
        renderer.setYLabelsColor(0, Color.BLACK);
        renderer.setXLabelsColor(Color.BLACK);
        renderer.setXRoundedLabels(false);

        renderer.setShowLegend(false);
        renderer.setShowGrid(true);

        renderer.setAxesColor(Color.BLACK);
        renderer.setGridColor(Color.GRAY);
    }

    private void renderInApp() {
        renderer.setYLabelsPadding(Helper.getDPI(activity, Helper.getDPI(activity, 2.5f)));
        renderer.setYAxisAlign(Paint.Align.LEFT, 0);
        renderer.setYLabelsAlign(Paint.Align.RIGHT);

        switch(activity.getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                renderer.setXLabels(12);
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                renderer.setXLabels(24);
        }
        renderer.setXAxisMin(0);
        renderer.setXAxisMax(24);

        renderer.setYLabels(6);
        float minimum = new PreferenceHelper(activity).
                formatDefaultToCustomUnit(Event.Category.BloodSugar,
                        activity.getResources().getIntArray(R.array.bloodsugar_extrema)[0]);
        renderer.setYAxisMin(minimum);
    }
}
