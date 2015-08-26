package com.faltenreich.diaguard.ui.chart;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.faltenreich.diaguard.fragments.ChartDayFragment;

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

    public ChartViewPager(Context context) {
        super(context);
    }

    public ChartViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setup(FragmentManager fragmentManager, ChartViewPagerCallback chartViewPagerCallback) {
        callback = chartViewPagerCallback;
        adapter = new ChartPagerAdapter(fragmentManager, DateTime.now());
        setAdapter(adapter);

        addOnPageChangeListener(new OnPageChangeListener() {
            private int currentPage;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    switch (Page.values()[currentPage]) {
                        case LEFT:
                            adapter.swipeTo(true);
                            break;
                        case RIGHT:
                            adapter.swipeTo(false);
                            break;
                    }
                    adapter.notifyDataSetChanged();
                    setCurrentItem(adapter.getMiddle(), false);
                }
            }
            @Override
            public void onPageSelected(int position) {
                this.currentPage = position;
                ChartDayFragment selectedFragment = (ChartDayFragment) adapter.getItem(position);
                if (selectedFragment != null && selectedFragment.getDateTime() != null) {
                    callback.onDateSelected(selectedFragment.getDateTime());
                }
            }
        });

        setCurrentItem(adapter.getMiddle(), false);
    }

    public void setDateTime(DateTime dateTime) {
        adapter.setDateTime(dateTime);
    }

    public interface ChartViewPagerCallback {
        void onDateSelected(DateTime day);
    }
}
