package com.faltenreich.diaguard.ui.chart;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import org.joda.time.DateTime;

/**
 * Created by Faltenreich on 01.08.2015.
 */
public class ChartPagerAdapter extends PagerAdapter {

    protected Context context;

    public ChartPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        DayChart chart = new DayChart(context);
        container.addView(chart);
        return chart;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return 3;
    }

    public int getMiddle() {
        return getCount() / 2;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}