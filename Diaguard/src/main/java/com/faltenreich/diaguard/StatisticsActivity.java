package com.faltenreich.diaguard;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.faltenreich.diaguard.fragments.BaseFragment;
import com.faltenreich.diaguard.fragments.OverviewFragment;
import com.faltenreich.diaguard.fragments.TrendFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class StatisticsActivity extends BaseActivity {

    public static final String EXTRA_TAB = "com.faltenreich.diaguard.fragments.StatisticsActivity.EXTRA_TAB";

    @Bind(R.id.viewpager)
    protected ViewPager viewPager;

    @Bind(R.id.tablayout)
    protected TabLayout tabLayout;

    public StatisticsActivity() {
        super(R.layout.activity_statistics);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewPager();
        checkForIntents();
    }

    private void initViewPager() {
        viewPager.setAdapter(new TabFragmentPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
    }

    private void checkForIntents() {
        Intent intent = getIntent();
        if (intent != null) {
            int tab = intent.getIntExtra(EXTRA_TAB, -1);
            // TODO: Check if tab < viewPager.items
            if (tab >= 0) {
                viewPager.setCurrentItem(tab);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class TabFragmentPagerAdapter extends FragmentPagerAdapter {

        private List<BaseFragment> fragments;

        public TabFragmentPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            fragments = new ArrayList<>();
            fragments.add(new OverviewFragment());
            fragments.add(new TrendFragment());
        }

        @Override
        public int getCount() {
            return fragments.size();
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
}
