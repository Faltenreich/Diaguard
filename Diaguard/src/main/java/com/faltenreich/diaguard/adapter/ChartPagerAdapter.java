package com.faltenreich.diaguard.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.core.widget.NestedScrollView;

import com.faltenreich.diaguard.ui.fragment.ChartDayFragment;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Faltenreich on 01.08.2015.
 */
public class ChartPagerAdapter extends FragmentStatePagerAdapter {

    private static final int ITEM_COUNT = 3;

    private List<ChartDayFragment> fragments;

    public ChartPagerAdapter(FragmentManager fragmentManager, DateTime dateTime, NestedScrollView.OnScrollChangeListener onScrollListener) {
        super(fragmentManager);

        fragments = new ArrayList<>();
        for (int position = 0; position < ITEM_COUNT; position++) {
            DateTime day = dateTime.minusDays(getMiddle()).plusDays(position);
            ChartDayFragment fragment = ChartDayFragment.createInstance(day);
            fragment.setOnScrollListener(onScrollListener);
            fragments.add(fragment);
        }
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return object instanceof ChartDayFragment ? fragments.indexOf(object) : POSITION_NONE;
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
                ChartDayFragment fragment = fragments.get(position);
                if (fragment != null) {
                    DateTime fragmentDay = day.minusDays(getMiddle()).plusDays(position);
                    fragment.setDay(fragmentDay);
                }
            }
        }
    }

    public synchronized void previousDay() {
        Collections.rotate(fragments, 1);
        notifyDataSetChanged();

        ChartDayFragment fragment = fragments.get(0);
        DateTime previousDay = fragment.getDay().minusDays(ITEM_COUNT);
        fragment.setDay(previousDay);
    }

    public synchronized void nextDay() {
        Collections.rotate(fragments, -1);
        notifyDataSetChanged();

        ChartDayFragment fragment = fragments.get(ITEM_COUNT - 1);
        DateTime nextDay = fragment.getDay().plusDays(ITEM_COUNT);
        fragment.setDay(nextDay);
    }
}