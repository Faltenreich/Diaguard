package com.faltenreich.diaguard.feature.timeline;


import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.event.data.EntryAddedEvent;
import com.faltenreich.diaguard.shared.event.data.EntryDeletedEvent;
import com.faltenreich.diaguard.shared.event.data.EntryUpdatedEvent;
import com.faltenreich.diaguard.shared.event.file.BackupImportedEvent;
import com.faltenreich.diaguard.shared.event.preference.CategoryPreferenceChangedEvent;
import com.faltenreich.diaguard.shared.event.preference.UnitChangedEvent;
import com.faltenreich.diaguard.shared.view.ViewUtils;
import com.faltenreich.diaguard.shared.view.fragment.DateFragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import butterknife.BindView;

public class TimelineFragment extends DateFragment implements TimelineViewPager.Listener {

    @BindView(R.id.viewpager) TimelineViewPager viewPager;

    public TimelineFragment() {
        super(R.layout.fragment_timeline, R.string.timeline, R.menu.timeline);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager.setup(getChildFragmentManager(), this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_today:
                goToDay(DateTime.now());
                return true;
            case R.id.action_style:
                openDialogForChartStyle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    private void openDialogForChartStyle() {
        if (getContext() != null) {
            TimelineStyle[] styles = TimelineStyle.values();
            String[] titles = new String[styles.length];
            for (int index = 0; index < styles.length; index++) {
                titles[index] = getString(styles[index].getTitleRes());
            }
            TimelineStyle currentStyle = PreferenceStore.getInstance().getTimelineStyle();
            new AlertDialog.Builder(getContext())
                .setTitle(R.string.chart_style)
                .setSingleChoiceItems(titles, currentStyle.getStableId(), null)
                .setNegativeButton(R.string.cancel, (dialog, which) -> {})
                .setPositiveButton(R.string.ok, (dialog, which) -> {
                    int position = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                    TimelineStyle style = styles[position];
                    PreferenceStore.getInstance().setTimelineStyle(style);
                    goToDay(getDay());
                })
            .create()
            .show();
        }
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(@SuppressWarnings("unused") CategoryPreferenceChangedEvent event) {
        if (isAdded()) {
            viewPager.reset();
        }
    }
}
