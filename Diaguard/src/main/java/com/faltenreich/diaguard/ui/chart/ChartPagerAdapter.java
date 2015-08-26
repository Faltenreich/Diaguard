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
import java.util.List;

/**
 * Created by Faltenreich on 01.08.2015.
 */
public class ChartPagerAdapter extends FragmentPagerAdapter {

    private static final int ITEM_COUNT = 3;

    private ChartDayFragment[] fragments;

    public ChartPagerAdapter(FragmentManager fragmentManager, DateTime dateTime) {
        super(fragmentManager);

        fragments = new ChartDayFragment[ITEM_COUNT];
        fragments[0] = ChartDayFragment.createInstance(dateTime.minusDays(1));
        fragments[1] = ChartDayFragment.createInstance(dateTime);
        fragments[2] = ChartDayFragment.createInstance(dateTime.plusDays(1));
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return ITEM_COUNT;
    }

    public int getMiddle() {
        return getCount() / 2;
    }
}