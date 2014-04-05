package com.android.diaguard.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Filip on 05.01.14.
 */
public class DynamicPageAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> pages;

    public DynamicPageAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        pages = new ArrayList<Fragment>();
    }

    @Override
    public Fragment getItem(int position) {
        return pages.get(position);
    }

    @Override
    public int getItemPosition(Object object){
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    public void addItem(Fragment fragment) {
        pages.add(fragment);
    }

    public void addItemAt(Fragment fragment, int position) {
        pages.add(position, fragment);
    }

    public void removeItemAt(int position) {
        pages.remove(position);
    }
}
