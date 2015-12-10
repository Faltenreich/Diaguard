package com.faltenreich.diaguard.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

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
        return getCount() >= 1 ? getCount() / 2 : 0;
    }

    public void setDay(DateTime day) {
        for (int position = 0; position < ITEM_COUNT; position++) {
            fragments.get(position).setDay(day.minusDays(getMiddle()).plusDays(position));
        }
        notifyDataSetChanged();
    }

    public void previousDay() {
        Collections.rotate(fragments, 1);
        ChartDayFragment fragment = fragments.get(0);
        DateTime previousDay = fragment.getDay().minusDays(ITEM_COUNT);
        fragment.setDay(previousDay);
        notifyDataSetChanged();
    }

    public void nextDay() {
        Collections.rotate(fragments, -1);
        ChartDayFragment fragment = fragments.get(ITEM_COUNT - 1);
        DateTime nextDay = fragment.getDay().plusDays(ITEM_COUNT);
        fragment.setDay(nextDay);
        notifyDataSetChanged();
    }
}