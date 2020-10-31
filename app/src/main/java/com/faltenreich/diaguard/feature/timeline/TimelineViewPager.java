package com.faltenreich.diaguard.feature.timeline;

import android.content.Context;
import android.util.AttributeSet;

import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.faltenreich.diaguard.feature.timeline.day.TimelineDayFragment;

import org.joda.time.DateTime;

/**
 * Created by Faltenreich on 02.08.2015.
 */
public class TimelineViewPager extends ViewPager {

    interface Listener {
        void onDaySelected(DateTime day);
    }

    private TimelineViewPagerAdapter adapter;
    private OnPageChangeListener onPageChangeListener;
    private int scrollOffset;

    public TimelineViewPager(Context context) {
        super(context);
    }

    public TimelineViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    void setup(final FragmentManager fragmentManager, final Listener callback) {
        adapter = new TimelineViewPagerAdapter(
            fragmentManager,
            DateTime.now(),
            (view, scrollX, scrollY, oldScrollX, oldScrollY) -> scrollOffset = scrollY
        );
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
                    if (position != adapter.getMiddle() && adapter.getItem(position) instanceof TimelineDayFragment) {
                        TimelineDayFragment fragment = (TimelineDayFragment) adapter.getItem(position);
                        fragment.scrollTo(scrollOffset);
                        callback.onDaySelected(fragment.getDay());
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    if (state == ViewPager.SCROLL_STATE_IDLE) {
                        int currentItem = getCurrentItem();
                        int targetItem = adapter.getMiddle();

                        ((TimelineDayFragment) adapter.getItem(currentItem)).update();

                        if (currentItem != targetItem) {
                            switch (currentItem) {
                                case 0:
                                    adapter.previousDay();
                                    break;
                                case 2:
                                    adapter.nextDay();
                                    break;
                            }
                            setCurrentItem(targetItem, false);
                        }
                    }
                }
            };
        } else {
            removeOnPageChangeListener(onPageChangeListener);
        }
        addOnPageChangeListener(onPageChangeListener);
    }

    void setDay(DateTime day) {
        adapter.setDay(day);
    }

    void reset() {
        adapter.reset();
    }
}
