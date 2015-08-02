package com.faltenreich.diaguard.ui.chart;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import org.joda.time.DateTime;
import org.joda.time.Days;

/**
 * Created by Faltenreich on 02.08.2015.
 */
public class ChartViewPager extends ViewPager {

    private ChartViewPagerCallback callback;
    private ChartPagerAdapter adapter;

    public ChartViewPager(Context context) {
        super(context);
    }

    public ChartViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setup(ChartViewPagerCallback chartViewPagerCallback) {
        this.callback = chartViewPagerCallback;
        adapter = new ChartPagerAdapter(getContext());
        setAdapter(adapter);
        setOffscreenPageLimit(1);
        setCurrentItem(adapter.getCount());

        addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                DateTime day = DateTime.now().minusDays(adapter.getCount() - position - 1);
                callback.onDateSelected(day);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public void setCurrentDate(DateTime day) {
        int position = adapter.getCount() - Days.daysBetween(day, DateTime.now()).getDays() - 1;
        setCurrentItem(position);
    }

    public interface ChartViewPagerCallback {
        void onDateSelected(DateTime day);
    }
}
