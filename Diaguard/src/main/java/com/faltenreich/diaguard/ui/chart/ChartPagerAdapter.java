package com.faltenreich.diaguard.ui.chart;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.faltenreich.diaguard.fragments.ChartDayFragment;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
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
        setDateTime(dateTime);
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

    public void setDateTime(DateTime dateTime) {
        fragments.clear();
        for (int position = 0; position < ITEM_COUNT; position++) {
            DateTime dateTimeOfFragment = dateTime.minusDays(getMiddle()).plusDays(position);
            fragments.add(ChartDayFragment.createInstance(dateTimeOfFragment));
        }
        notifyDataSetChanged();
    }

    public void swipeTo(boolean toLeft) {
        if (toLeft) {
            ChartDayFragment fragment = fragments.get(ITEM_COUNT - 1);
            fragments.remove(fragment);
            fragments.add(0, fragment);
            fragment.setDateTime(fragment.getDateTime().minusDays(ITEM_COUNT));
        } else {
            ChartDayFragment fragment = fragments.get(0);
            fragments.remove(fragment);
            fragments.add(fragment);
            fragment.setDateTime(fragment.getDateTime().plusDays(ITEM_COUNT));
        }
    }
}