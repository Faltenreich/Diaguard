package com.faltenreich.diaguard.ui.fragment;


import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.event.BackupImportedEvent;
import com.faltenreich.diaguard.data.event.data.EntryAddedEvent;
import com.faltenreich.diaguard.data.event.data.EntryDeletedEvent;
import com.faltenreich.diaguard.data.event.data.EntryUpdatedEvent;
import com.faltenreich.diaguard.data.event.preference.UnitChangedEvent;
import com.faltenreich.diaguard.ui.viewpager.ChartViewPager;
import com.faltenreich.diaguard.util.ViewUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import butterknife.BindView;

public class ChartFragment extends DateFragment implements ChartViewPager.ChartViewPagerCallback {

    @BindView(R.id.viewpager) ChartViewPager viewPager;

    public ChartFragment() {
        super(R.layout.fragment_chart, R.string.timeline);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager.setup(getChildFragmentManager(), this);
    }

    @Override
    public String getTitle() {
        boolean showShortText = !ViewUtils.isLandscape(getActivity()) && !ViewUtils.isLargeScreen(getActivity());
        String weekDay = showShortText ?
                getDay().dayOfWeek().getAsShortText() :
                getDay().dayOfWeek().getAsText();
        String date = DateTimeFormat.mediumDate().print(getDay());
        return String.format("%s, %s", weekDay, date);
    }

    @Override
    protected void goToDay(DateTime day) {
        super.goToDay(day);
        viewPager.setDay(day);
    }

    @Override
    public void onDaySelected(DateTime day) {
        if (day != null) {
            setDay(day);
            updateLabels();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EntryAddedEvent event) {
        if (isAdded()) {
            goToDay(getDay());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EntryDeletedEvent event) {
        super.onEvent(event);
        if (isAdded()) {
            goToDay(getDay());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EntryUpdatedEvent event) {
        if (isAdded()) {
            goToDay(getDay());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UnitChangedEvent event) {
        if (isAdded()) {
            goToDay(getDay());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BackupImportedEvent event) {
        if (isAdded()) {
            goToDay(getDay());
        }
    }
}
