package com.faltenreich.diaguard.feature.timeline;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Faltenreich on 01.08.2015.
 */
public class TimelinePagerAdapter extends FragmentStatePagerAdapter {

    private static final int ITEM_COUNT = 3;

    private final List<TimelineDayFragment> fragments;

    TimelinePagerAdapter(
        FragmentManager fragmentManager,
        DateTime dateTime,
        NestedScrollView.OnScrollChangeListener onScrollListener
    ) {
        super(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        fragments = new ArrayList<>();
        for (int position = 0; position < ITEM_COUNT; position++) {
            DateTime day = dateTime.minusDays(getMiddle()).plusDays(position);
            TimelineDayFragment fragment = TimelineDayFragment.createInstance(day);
            fragment.setOnScrollListener(onScrollListener);
            fragments.add(fragment);
        }
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return object instanceof TimelineDayFragment ? fragments.indexOf(object) : POSITION_NONE;
    }

    @Override
    public int getCount() {
        return ITEM_COUNT;
    }

    public int getMiddle() {
        return getCount() >= 1 ? getCount() / 2 : 0;
    }

    public void setDay(DateTime day) {
        for (int position = 0; position < ITEM_COUNT; position++) {
            if (position < fragments.size()) {
                TimelineDayFragment fragment = fragments.get(position);
                if (fragment != null) {
                    DateTime fragmentDay = day.minusDays(getMiddle()).plusDays(position);
                    fragment.setDay(fragmentDay);
                }
            }
        }
    }

    synchronized void previousDay() {
        Collections.rotate(fragments, 1);
        notifyDataSetChanged();

        TimelineDayFragment fragment = fragments.get(0);
        DateTime previousDay = fragment.getDay().minusDays(ITEM_COUNT);
        fragment.setDay(previousDay);
    }

    synchronized void nextDay() {
        Collections.rotate(fragments, -1);
        notifyDataSetChanged();

        TimelineDayFragment fragment = fragments.get(ITEM_COUNT - 1);
        DateTime nextDay = fragment.getDay().plusDays(ITEM_COUNT);
        fragment.setDay(nextDay);
    }

    void reset() {
        for (int position = 0; position < ITEM_COUNT; position++) {
            if (position < fragments.size()) {
                TimelineDayFragment fragment = fragments.get(position);
                fragment.reset();
            }
        }
    }
}