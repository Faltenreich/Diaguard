package com.faltenreich.diaguard.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.list.StatisticsFragmentPagerAdapter;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.ui.fragment.StatisticsFragment;
import com.faltenreich.diaguard.util.TimeSpan;
import com.faltenreich.diaguard.util.event.Event;
import com.faltenreich.diaguard.util.event.Events;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by Filip on 15.11.13.
 */

public class StatisticsActivity extends BaseActivity {

    @Bind(R.id.statistics_tablayout)
    protected TabLayout tabLayout;

    @Bind(R.id.statistics_viewpager)
    protected ViewPager viewPager;

    private StatisticsFragmentPagerAdapter pagerAdapter;

    private TimeSpan timeSpan;

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
        menu.findItem(R.id.action_time_interval).setTitle(timeSpan.toString());
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_time_interval:
                skipTimeInterval();
                item.setTitle(timeSpan.toString());
                return true;
        }
        return false;
    }

    private void init() {
        timeSpan = TimeSpan.WEEK;
        initLayout();
    }

    private void initLayout() {
        Measurement.Category[] categories = PreferenceHelper.getInstance().getActiveCategories();
        ArrayList<StatisticsFragment> fragments = new ArrayList<>();
        for (Measurement.Category category : categories) {
            fragments.add(StatisticsFragment.newInstance(category, timeSpan));
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
        int nextOrdinal = timeSpan.ordinal() + 1;
        TimeSpan nextTimeSpan = TimeSpan.values()[nextOrdinal < TimeSpan.values().length ? nextOrdinal : 0];
        setTimeSpan(nextTimeSpan);
    }

    private void setTimeSpan(TimeSpan timeSpan) {
        this.timeSpan = timeSpan;
        Events.post(new Event.TimeSpanChangedEvent(timeSpan));
    }
}