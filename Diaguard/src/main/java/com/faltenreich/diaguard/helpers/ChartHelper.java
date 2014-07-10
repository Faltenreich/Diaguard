package com.faltenreich.diaguard.helpers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.database.Event;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

/**
 * Created by Filip on 01.12.13.
 */
public class ChartHelper {

    public static final int CHART_OFFSET_LEFT = 3;
    public static final int CHART_OFFSET_RIGHT = 1;

    public enum ChartType {
        LineChart,
        ScatterChart
    }

    Activity activity;
    ChartType chartType;

    public XYMultipleSeriesDataset seriesDataset;
    public XYMultipleSeriesRenderer renderer;
    public GraphicalView chartView;

    public ChartHelper(Activity activity, ChartType chartType) {
        this.seriesDataset = new XYMultipleSeriesDataset();
        this.renderer = new XYMultipleSeriesRenderer();
        this.activity = activity;
        this.chartType = chartType;
    }

    public void initialize() {
        // Needed for empty chart to render labels correctly (WTF?)
        XYSeriesRenderer rendererFake = new XYSeriesRenderer();
        renderer.addSeriesRenderer(rendererFake);
        XYSeries seriesFake = new XYSeries("Fake");
        seriesDataset.addSeries(seriesFake);
        seriesFake.add(-999, -999);

        if(chartType == ChartType.LineChart)
            chartView = ChartFactory.getLineChartView(activity, seriesDataset, renderer);
        else
            chartView = ChartFactory.getScatterChartView(activity, seriesDataset, renderer);
    }

    public void render() {
        renderer.setClickEnabled(true);
        renderer.setSelectableBuffer((int)Helper.getDPI(activity, 10));
        renderer.setAntialiasing(true);

        renderer.setPanEnabled(false , false);
        renderer.setZoomEnabled(false, false);
        renderer.setShowLegend(false);
        renderer.setShowGrid(false);
        renderer.setGridColor(activity.getResources().getColor(R.color.semitransparent_light));
        renderer.setAxesColor(Color.DKGRAY);
        renderer.setApplyBackgroundColor(true);
        renderer.setBackgroundColor(Color.argb(0x00, 0xff, 0x00, 0x00));
        renderer.setPointSize(Helper.getDPI(activity, 7));

        renderer.setMargins(new int[]{0, 0, 0, 0});
        renderer.setMarginsColor(activity.getResources().getColor(R.color.gray_lt));

        renderer.setXRoundedLabels(false);
        renderer.setLabelsTextSize(Helper.getDPI(activity, 12));
        renderer.setXLabelsColor(Color.GRAY);
        renderer.setYLabelsColor(0, Color.WHITE);
        renderer.setYLabelsAlign(Paint.Align.LEFT);
        renderer.setYLabelsPadding(Helper.getDPI(activity, -10));

        // Time
        renderer.setXAxisMin(0 - CHART_OFFSET_LEFT);
        renderer.setXAxisMax(24 + CHART_OFFSET_RIGHT);
        renderer.setXLabels(0);
        for(int hour = 0; hour <= 24; hour = hour + 2)
            renderer.addXTextLabel(hour, Integer.toString(hour));

        renderer.setYLabels(8);
        float minimum = new PreferenceHelper(activity).
                formatDefaultToCustomUnit(Event.Category.BloodSugar,
                        activity.getResources().getIntArray(R.array.bloodsugar_extrema)[0]);
        renderer.setYAxisMin(minimum);
    }

    public static XYSeriesRenderer getSeriesRendererForBloodSugar(Context context) {
        XYSeriesRenderer seriesRendererBloodSugar = new XYSeriesRenderer();
        seriesRendererBloodSugar.setColor(Color.WHITE);
        seriesRendererBloodSugar.setLineWidth(Helper.getDPI(context, 3));
        seriesRendererBloodSugar.setPointStyle(PointStyle.CIRCLE);
        seriesRendererBloodSugar.setFillPoints(false);
        seriesRendererBloodSugar.setPointStrokeWidth(Helper.getDPI(context, 3));
        return seriesRendererBloodSugar;
    }
}
