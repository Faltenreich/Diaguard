package com.faltenreich.diaguard.ui.chart;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.faltenreich.diaguard.ui.chart.DayChart;

import org.joda.time.DateTime;

import java.util.ArrayList;

/**
 * Created by Faltenreich on 01.08.2015.
 */
public abstract class EndlessPagerAdapter extends PagerAdapter {

    protected Context context;

    public EndlessPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public void destroyItem (ViewGroup container, int position, Object object)
    {
        container.removeView ((View) object);
    }

    @Override
    public int getCount ()
    {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject (View view, Object object)
    {
        return view == object;
    }
}