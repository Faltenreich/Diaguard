package com.faltenreich.diaguard.ui.chart;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import org.joda.time.DateTime;

/**
 * Created by Faltenreich on 02.08.2015.
 */
public class ChartViewPager extends ViewPager {

    private enum Page {
        LEFT,
        MIDDLE,
        RIGHT
    }

    private ChartViewPagerCallback callback;
    private ChartPagerAdapter adapter;

    private DateTime dateTime;

    public ChartViewPager(Context context) {
        super(context);
    }

    public ChartViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setup(ChartViewPagerCallback chartViewPagerCallback) {
        callback = chartViewPagerCallback;
        adapter = new ChartPagerAdapter(getContext());
        setAdapter(adapter);

        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    updatePages();
                    setCurrentItem(adapter.getMiddle(), false);
                }
            }
            @Override
            public void onPageSelected(int position) {
                switch (Page.values()[position]) {
                    case LEFT:
                        dateTime = dateTime.minusDays(1);
                        break;
                    case RIGHT:
                        dateTime = dateTime.plusDays(1);
                        break;
                }
                callback.onDateSelected(dateTime);
            }
        });

        dateTime = DateTime.now();
        setCurrentItem(adapter.getMiddle(), false);
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
        updatePages();
    }

    private void updatePages() {
        for (int position = 0; position < adapter.getCount(); position++) {
            View childView = getChildAt(position);
            if (childView instanceof DayChart) {
                DayChart dayChart = (DayChart) childView;
                switch (Page.values()[position]) {
                    case LEFT:
                        dayChart.setDateTime(dateTime.minusDays(1));
                        break;
                    case MIDDLE:
                        dayChart.setDateTime(dateTime);
                        break;
                    case RIGHT:
                        dayChart.setDateTime(dateTime.plusDays(1));
                        break;
                }
            }
        }
    }

    public interface ChartViewPagerCallback {
        void onDateSelected(DateTime day);
    }
}
