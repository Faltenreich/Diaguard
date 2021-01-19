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
    private final DateTime dateTime;
    private final NestedScrollView.OnScrollChangeListener onScrollListener;

    TimelinePagerAdapter(
        FragmentManager fragmentManager,
        DateTime dateTime,
        NestedScrollView.OnScrollChangeListener onScrollListener
    ) {
        super(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fragments = new ArrayList<>();
        this.dateTime = dateTime;
        this.onScrollListener = onScrollListener;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        DateTime day = dateTime.minusDays(getMiddle()).plusDays(position);
        TimelineDayFragment fragment = TimelineDayFragment.createInstance(day);
        // FIXME: Fix memory leak by removing fragment property, e.g. via shared view model
        fragment.setOnScrollListener(onScrollListener);
        fragments.add(position < fragments.size() ? 0 : fragments.size(), fragment);
        return fragment;
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

    @NonNull
    TimelineDayFragment getFragment(int position) {
        return fragments.get(position);
    }

    void setDay(DateTime day) {
        for (int position = 0; position < getCount(); position++) {
            TimelineDayFragment fragment = getFragment(position);
            DateTime fragmentDay = day.minusDays(getMiddle()).plusDays(position);
            fragment.setDay(fragmentDay);
        }
    }

    synchronized void previousDay() {
        Collections.rotate(fragments, 1);
        notifyDataSetChanged();

        TimelineDayFragment fragment = getFragment(0);
        DateTime previousDay = fragment.getDay().minusDays(getCount());
        fragment.setDay(previousDay);
    }

    synchronized void nextDay() {
        Collections.rotate(fragments, -1);
        notifyDataSetChanged();

        TimelineDayFragment fragment = getFragment(getCount() - 1);
        DateTime nextDay = fragment.getDay().plusDays(getCount());
        fragment.setDay(nextDay);
    }

    void reset() {
        for (int position = 0; position < getCount(); position++) {
            TimelineDayFragment fragment = getFragment(position);
            fragment.reset();
        }
    }
}