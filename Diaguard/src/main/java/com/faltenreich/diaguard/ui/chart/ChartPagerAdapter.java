package com.faltenreich.diaguard.ui.chart;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.faltenreich.diaguard.fragments.ChartDayFragment;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Faltenreich on 01.08.2015.
 */
public class ChartPagerAdapter extends FragmentPagerAdapter {

    private static final int ITEM_COUNT = 3;

    private List<ChartDayFragment> fragments;

    public ChartPagerAdapter(FragmentManager fragmentManager, DateTime dateTime) {
        super(fragmentManager);

        fragments = new ArrayList<>();
        for (int position = 0; position < ITEM_COUNT; position++) {
            DateTime dateTimeOfFragment = dateTime.minusDays(getMiddle()).plusDays(position);
            fragments.add(ChartDayFragment.createInstance(dateTimeOfFragment));
        }
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return object instanceof ChartDayFragment ? fragments.indexOf(object) : -1;
    }

    @Override
    public int getCount() {
        return ITEM_COUNT;
    }

    public int getMiddle() {
        return getCount() / 2;
    }

    public void setDay(DateTime day) {
        for (int position = 0; position < ITEM_COUNT; position++) {
            ChartDayFragment fragment = fragments.get(0);
            DateTime dateTimeOfFragment = day.minusDays(getMiddle()).plusDays(position);
            fragment.setDay(dateTimeOfFragment);
        }
        notifyDataSetChanged();
    }

    public void nextDay() {
        ChartDayFragment fragment = fragments.get(0);
        fragments.remove(fragment);
        fragments.add(fragment);
        fragment.setDay(fragment.getDay().plusDays(ITEM_COUNT));
        notifyDataSetChanged();
    }

    public void previousDay() {
        ChartDayFragment fragment = fragments.get(ITEM_COUNT - 1);
        fragments.remove(fragment);
        fragments.add(0, fragment);
        fragment.setDay(fragment.getDay().minusDays(ITEM_COUNT));
        notifyDataSetChanged();
    }
}