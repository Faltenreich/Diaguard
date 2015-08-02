package com.faltenreich.diaguard.ui.chart;

import android.content.Context;
import android.view.ViewGroup;

import org.joda.time.DateTime;

/**
 * Created by Faltenreich on 01.08.2015.
 */
public class ChartPagerAdapter extends EndlessPagerAdapter {

    public ChartPagerAdapter(Context context) {
        super(context);
    }

    @Override
    public Object instantiateItem (ViewGroup container, int position)
    {
        DayChart chart = new DayChart(context, DateTime.now().minusDays(Integer.MAX_VALUE - position - 1));
        container.addView(chart, 0);
        chart.setup();
        return chart;
    }
}