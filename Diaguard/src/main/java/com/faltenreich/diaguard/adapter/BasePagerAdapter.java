package com.faltenreich.diaguard.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.faltenreich.diaguard.ui.fragment.BaseFragment;

import java.util.ArrayList;

/**
 * Created by Faltenreich on 02.02.2016.
 */
public class BasePagerAdapter<T extends BaseFragment> extends FragmentPagerAdapter {

    private ArrayList<T> fragments;

    public BasePagerAdapter(FragmentManager fragmentManager, ArrayList<T> fragments) {
        super(fragmentManager);
        this.fragments = fragments;
    }

    public BasePagerAdapter(FragmentManager fragmentManager, T[] fragments) {
        super(fragmentManager);
    }

    @Override
    public int getCount() {
        return fragments != null ? fragments.size() : 0;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragments.get(position).getTitle();
    }
}
