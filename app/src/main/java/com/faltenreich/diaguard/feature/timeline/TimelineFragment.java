package com.faltenreich.diaguard.feature.timeline;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.FragmentTimelineBinding;
import com.faltenreich.diaguard.feature.datetime.DatePicking;
import com.faltenreich.diaguard.feature.datetime.DateTimeUtils;
import com.faltenreich.diaguard.feature.entry.edit.EntryEditFragment;
import com.faltenreich.diaguard.feature.navigation.MainButton;
import com.faltenreich.diaguard.feature.navigation.MainButtonProperties;
import com.faltenreich.diaguard.feature.navigation.ToolbarDescribing;
import com.faltenreich.diaguard.feature.navigation.ToolbarProperties;
import com.faltenreich.diaguard.feature.preference.timeline.TimelinePreferenceFragment;
import com.faltenreich.diaguard.shared.event.data.EntryAddedEvent;
import com.faltenreich.diaguard.shared.event.data.EntryDeletedEvent;
import com.faltenreich.diaguard.shared.event.data.EntryUpdatedEvent;
import com.faltenreich.diaguard.shared.event.file.BackupImportedEvent;
import com.faltenreich.diaguard.shared.event.preference.CategoryPreferenceChangedEvent;
import com.faltenreich.diaguard.shared.event.preference.TimelinePreferenceChangedEvent;
import com.faltenreich.diaguard.shared.event.preference.UnitChangedEvent;
import com.faltenreich.diaguard.shared.view.ViewUtils;
import com.faltenreich.diaguard.shared.view.fragment.BaseFragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class TimelineFragment
    extends BaseFragment<FragmentTimelineBinding>
    implements ToolbarDescribing, MainButton, DatePicking, ViewPager.OnPageChangeListener
{

    private ViewPager viewPager;
    private TimelinePagerAdapter adapter;

    private DateTime day;
    private int scrollOffset;

    @Override
    protected FragmentTimelineBinding createBinding(LayoutInflater layoutInflater) {
        return FragmentTimelineBinding.inflate(layoutInflater);
    }

    @Override
    public ToolbarProperties getToolbarProperties() {
        String title = null;
        if (day != null) {
            boolean isLargeTitle = ViewUtils.isLandscape(getActivity()) || ViewUtils.isLargeScreen(getActivity());
            String weekDay = isLargeTitle ?
                day.dayOfWeek().getAsText() :
                day.dayOfWeek().getAsShortText();
            String date = DateTimeFormat.mediumDate().print(day);
            title = String.format("%s, %s", weekDay, date);
        }
        return new ToolbarProperties.Builder()
            .setTitle(title)
            .setMenu(R.menu.timeline)
            .setOnClickListener((view) -> showDatePicker(day, getParentFragmentManager()))
            .build();
    }

    @Override
    public MainButtonProperties getMainButtonProperties() {
        return MainButtonProperties.addButton(view -> {
            if (getContext() != null) {
                // Date will not be passed through to compensate negative user feedback
                openFragment(new EntryEditFragment(), true);
            }
        }, true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews();
        initLayout();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updateLabels();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_today) {
            goToDay(DateTime.now());
            return true;
        } else if (itemId == R.id.action_settings) {
            openFragment(new TimelinePreferenceFragment(), true);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        day = DateTimeUtils.atStartOfDay(DateTime.now());
        adapter = new TimelinePagerAdapter(
            getChildFragmentManager(),
            day,
            (view, scrollX, scrollY, oldScrollX, oldScrollY) -> scrollOffset = scrollY
        );
    }

    private void bindViews() {
        viewPager = getBinding().viewPager;
    }

    private void initLayout() {
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(adapter.getMiddle(), false);
        viewPager.addOnPageChangeListener(this);
        // Prevent destroying offscreen fragments that occur on fast scrolling
        viewPager.setOffscreenPageLimit(2);
    }

    @Override
    public void goToDay(DateTime day) {
        this.day = day;
        updateLabels();
        adapter.setDay(day);
    }

    private void updateLabels() {
        setTitle(getToolbarProperties().getTitle());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        if (position != adapter.getMiddle() && position >= 0 && position < adapter.getCount()) {
            TimelineDayFragment fragment = adapter.getFragment(position);
            DateTime day = fragment.getDay();
            if (day != null) {
                fragment.scrollTo(scrollOffset);
                this.day = day;
                updateLabels();
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            int currentItem = viewPager.getCurrentItem();
            int targetItem = adapter.getMiddle();

            adapter.getFragment(currentItem).invalidateList();

            if (currentItem != targetItem) {
                switch (currentItem) {
                    case 0:
                        adapter.previousDay();
                        break;
                    case 2:
                        adapter.nextDay();
                        break;
                }
                viewPager.setCurrentItem(targetItem, false);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EntryAddedEvent event) {
        goToDay(day);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EntryDeletedEvent event) {
        super.onEvent(event);
        goToDay(day);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EntryUpdatedEvent event) {
        goToDay(day);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UnitChangedEvent event) {
        goToDay(day);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BackupImportedEvent event) {
        goToDay(day);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(CategoryPreferenceChangedEvent event) {
        adapter.reset();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(TimelinePreferenceChangedEvent event) {
        adapter.reset();
    }
}
