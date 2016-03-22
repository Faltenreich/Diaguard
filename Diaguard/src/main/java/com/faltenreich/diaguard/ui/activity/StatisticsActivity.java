package com.faltenreich.diaguard.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.list.StatisticsFragmentPagerAdapter;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.ui.fragment.StatisticsFragment;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by Filip on 15.11.13.
 */

public class StatisticsActivity extends BaseActivity {

    public enum TimeInterval {
        WEEK,
        TWO_WEEKS,
        MONTH,
        YEAR;

        @Override
        public String toString() {
            switch (this) {
                case WEEK:
                    return DiaguardApplication.getContext().getString(R.string.week);
                case TWO_WEEKS:
                    return DiaguardApplication.getContext().getString(R.string.week_two);
                case MONTH:
                    return DiaguardApplication.getContext().getString(R.string.month);
                case YEAR:
                    return DiaguardApplication.getContext().getString(R.string.year);
                default:
                    return super.toString();
            }
        }
    }

    @Bind(R.id.statistics_tablayout)
    protected TabLayout tabLayout;

    @Bind(R.id.statistics_viewpager)
    protected ViewPager viewPager;

    private StatisticsFragmentPagerAdapter pagerAdapter;

    private TimeInterval timeInterval;

    public StatisticsActivity() {
        super(R.layout.activity_statistics);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.date_interval, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_time_interval).setTitle(timeInterval.toString());
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_time_interval:
                skipTimeInterval();
                item.setTitle(timeInterval.toString());
                return true;
        }
        return false;
    }

    private void init() {
        timeInterval = TimeInterval.WEEK;
        initLayout();
    }

    private void initLayout() {
        Measurement.Category[] categories = PreferenceHelper.getInstance().getActiveCategories();
        ArrayList<StatisticsFragment> fragments = new ArrayList<>();
        for (Measurement.Category category : categories) {
            fragments.add(StatisticsFragment.newInstance(category, timeInterval));
        }
        pagerAdapter = new StatisticsFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        for (int tabPosition = 0; tabPosition < tabLayout.getTabCount(); tabPosition++) {
            Measurement.Category category = categories[tabPosition];
            int categoryImageResourceId = PreferenceHelper.getInstance().getCategoryImageResourceId(category);
            TabLayout.Tab tab = tabLayout.getTabAt(tabPosition);
            if (tab != null) {
                tab.setIcon(categoryImageResourceId);
                tab.setText(null);
            }
        }
    }

    private void skipTimeInterval() {
        int nextOrdinal = timeInterval.ordinal() + 1;
        TimeInterval nextTimeInterval = TimeInterval.values()[nextOrdinal < TimeInterval.values().length ? nextOrdinal : 0];
        setTimeInterval(nextTimeInterval);
    }

    private void setTimeInterval(TimeInterval timeInterval) {
        this.timeInterval = timeInterval;
        updateTimeIntervalForChildren();
    }

    private void updateTimeIntervalForChildren() {
        for (int fragmentPosition = 0; fragmentPosition < pagerAdapter.getCount(); fragmentPosition++) {
            Fragment fragment = pagerAdapter.getItem(fragmentPosition);
            if (fragment instanceof StatisticsFragment) {
                StatisticsFragment statisticsFragment = (StatisticsFragment) fragment;
                statisticsFragment.setTimeInterval(timeInterval);
                statisticsFragment.updateContent();
            }
        }
    }
}