package com.faltenreich.diaguard.ui.view.chart;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;

import com.faltenreich.diaguard.adapter.ChartPagerAdapter;
import com.faltenreich.diaguard.ui.fragment.ChartDayFragment;

import org.joda.time.DateTime;

/**
 * Created by Faltenreich on 02.08.2015.
 */
public class ChartViewPager extends ViewPager {

    private static final String TAG = ChartViewPager.class.getSimpleName();

    private ChartPagerAdapter adapter;
    private OnPageChangeListener onPageChangeListener;

    public ChartViewPager(Context context) {
        super(context);
    }

    public ChartViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setup(final FragmentManager fragmentManager, final ChartViewPagerCallback callback) {
        adapter = new ChartPagerAdapter(fragmentManager, DateTime.now());
        setAdapter(adapter);
        setCurrentItem(adapter.getMiddle(), false);

        // Prevent destroying offscreen fragments that occur on fast scrolling
        setOffscreenPageLimit(2);

        if (onPageChangeListener == null) {
            onPageChangeListener = new OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    if (position != adapter.getMiddle() && adapter.getItem(position) instanceof ChartDayFragment) {
                        Log.i(TAG, "onPageSelected " + position);
                        ChartDayFragment fragment = (ChartDayFragment) adapter.getItem(position);
                        callback.onDaySelected(fragment.getDay());
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    if (state == ViewPager.SCROLL_STATE_IDLE) {
                        int currentItem = getCurrentItem();
                        int targetItem = adapter.getMiddle();

                        if (currentItem != targetItem) {
                            Log.i(TAG, "onPageIdle " + currentItem);

                            switch (currentItem) {
                                case 0:
                                    adapter.previousDay();
                                    break;
                                case 2:
                                    adapter.nextDay();
                                    break;
                            }

                            setCurrentItem(targetItem, false);
                            Log.i(TAG, "Scrolled to page " + targetItem);
                        }
                    }
                }
            };
        } else {
            removeOnPageChangeListener(onPageChangeListener);
        }
        addOnPageChangeListener(onPageChangeListener);
    }

    public void setDay(DateTime day) {
        adapter.setDay(day);
    }

    public interface ChartViewPagerCallback {
        void onDaySelected(DateTime day);
    }
}
