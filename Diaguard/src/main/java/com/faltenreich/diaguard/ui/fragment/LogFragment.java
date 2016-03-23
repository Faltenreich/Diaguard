package com.faltenreich.diaguard.ui.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ProgressBar;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.list.ListItem;
import com.faltenreich.diaguard.adapter.list.ListItemDate;
import com.faltenreich.diaguard.adapter.SafeLinearLayoutManager;
import com.faltenreich.diaguard.adapter.StickyHeaderDecoration;
import com.faltenreich.diaguard.adapter.list.ListItemEmpty;
import com.faltenreich.diaguard.adapter.list.ListItemEntry;
import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.util.ViewHelper;
import com.faltenreich.diaguard.ui.view.DayOfMonthDrawable;
import com.faltenreich.diaguard.adapter.LogRecyclerAdapter;
import com.faltenreich.diaguard.util.event.Event;
import com.faltenreich.diaguard.util.event.Events;

import org.joda.time.DateTime;

import butterknife.Bind;

/**
 * Created by Filip on 05.07.2015.
 */
public class LogFragment extends BaseFragment implements BaseFragment.ToolbarCallback, LogRecyclerAdapter.OnAdapterChangesListener {

    @Bind(R.id.fragment_log_list)
    protected RecyclerView recyclerView;

    @Bind(R.id.fragment_log_progressbar)
    protected ProgressBar progressBar;

    private LogRecyclerAdapter listAdapter;
    private StickyHeaderDecoration listDecoration;
    private LinearLayoutManager listLayoutManager;

    public LogFragment() {
        super(R.layout.fragment_log);
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        initialize();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Events.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Events.unregister(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        updateMonthForUi(getFirstVisibleDay());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.date, menu);

        MenuItem menuItem = menu.findItem(R.id.action_today);
         if (menuItem != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                LayerDrawable icon = (LayerDrawable) menuItem.getIcon();
                setTodayIcon(icon, getActivity());
            } else {
                menuItem.setIcon(R.drawable.ic_action_today);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_today:
                goToDay(DateTime.now());
                break;
        }
        return true;
    }

    @Override
    public String getTitle() {
        return DiaguardApplication.getContext().getString(R.string.log);
    }

    @Override
    public void action() {
        showDatePicker();
    }

    private void initialize() {
        listLayoutManager = new SafeLinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(listLayoutManager);
        listAdapter = new LogRecyclerAdapter(getActivity(), this);
        recyclerView.setAdapter(listAdapter);
        listDecoration = new StickyHeaderDecoration(listAdapter, true);
        recyclerView.addItemDecoration(listDecoration);

        // Fragment updates
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                DateTime dateTime = getFirstVisibleDay();
                if (dateTime != null) {
                    // Update month in Toolbar when section is being crossed
                    boolean isScrollingUp = dy < 0;
                    boolean isCrossingMonth = isScrollingUp ?
                            dateTime.dayOfMonth().get() == dateTime.dayOfMonth().getMaximumValue() :
                            dateTime.dayOfMonth().get() == dateTime.dayOfMonth().getMinimumValue();
                    if (isCrossingMonth) {
                        updateMonthForUi(dateTime);
                    }
                }
            }
        });

        progressBar.setVisibility(View.VISIBLE);
        DateTime dateTime = DateTime.now();
        listAdapter.setup(dateTime);
        updateMonthForUi(dateTime);
    }

    private DateTime getFirstVisibleDay() {
        int firstVisibleItemPosition = listLayoutManager.findFirstVisibleItemPosition();
        if (firstVisibleItemPosition >= 0 && firstVisibleItemPosition < listAdapter.getItemCount()) {
            ListItemDate item = listAdapter.getItem(listLayoutManager.findFirstVisibleItemPosition());
            return item.getDateTime();
        } else {
            return null;
        }
    }

    private void goToDay(DateTime dateTime) {
        int positionOfDay = listAdapter.getDayPosition(dateTime);
        boolean containsDay = positionOfDay >= 0;
        if (containsDay) {
            recyclerView.scrollToPosition(positionOfDay);
        } else {
            progressBar.setVisibility(View.VISIBLE);
            listAdapter.setup(dateTime);
        }
        updateMonthForUi(dateTime);
    }

    private void updateMonthForUi(DateTime dateTime) {
        String format;
        if (ViewHelper.isLandscape(getActivity()) || ViewHelper.isLargeScreen(getActivity())) {
            format = "MMMM YYYY";
        } else {
            format = "MMM YYYY";
        }
        getActionView().setText(dateTime.toString(format));
    }

    private void setTodayIcon(LayerDrawable icon, Context context) {
        DayOfMonthDrawable today = new DayOfMonthDrawable(context);
        today.setDayOfMonth(DateTime.now().dayOfMonth().get());
        icon.mutate();
        icon.setDrawableByLayerId(R.id.today_icon_day, today);
    }

    public void showDatePicker () {
        DatePickerFragment fragment = new DatePickerFragment() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                goToDay(DateTime.now().withYear(year).withMonthOfYear(month + 1).withDayOfMonth(day));
            }
        };
        Bundle bundle = new Bundle(1);
        bundle.putSerializable(DatePickerFragment.DATE, getFirstVisibleDay());
        fragment.setArguments(bundle);
        fragment.show(getActivity().getSupportFragmentManager(), "DatePicker");
    }

    @Override
    public void onOrderChanges() {
        if (isAdded() && listDecoration != null) {
            listDecoration.clearHeaderCache();
        }
    }

    @Override
    public void onSetupComplete(DateTime dateTime) {
        if (isAdded()) {
            progressBar.setVisibility(View.GONE);
            goToDay(dateTime);
        }
    }

    private void updateHeaderSection(DateTime dateTime) {
        int position = listAdapter.getFirstListItemEntryOfDayPosition(dateTime);
        if (position >= 0) {
            ListItemEntry firstListItemEntry = (ListItemEntry) listAdapter.getItem(position);
            while (listAdapter.getItem(position).getDateTime().withTimeAtStartOfDay().isEqual(dateTime.withTimeAtStartOfDay()) &&
                    listAdapter.getItem(position) instanceof ListItemEntry) {
                ListItemEntry listItem = (ListItemEntry) listAdapter.getItem(position);
                listItem.setFirstListItemEntryOfDay(firstListItemEntry);
                position++;
            }
        }
        listDecoration.clearHeaderCache();
    }

    @SuppressWarnings("unused")
    public void onEvent(Event.EntryAddedEvent event) {
        if (isAdded()) {
            Entry entry = event.context;
            if (entry != null) {
                int entryPosition = listAdapter.getNextDateTimePosition(entry.getDate());
                if (entryPosition >= 0) {
                    // Remove any existing empty view
                    int emptyViewPosition = entryPosition - 1;
                    if (listAdapter.getItem(emptyViewPosition) instanceof ListItemEmpty) {
                        listAdapter.removeItem(emptyViewPosition);
                        listAdapter.notifyItemRemoved(emptyViewPosition);
                        entryPosition = emptyViewPosition;
                    }

                    entry.setMeasurementCache(EntryDao.getInstance().getMeasurements(entry));
                    ListItemEntry listItemEntry = new ListItemEntry(entry);
                    listAdapter.addItem(entryPosition, listItemEntry);
                    listAdapter.notifyItemInserted(entryPosition);

                    updateHeaderSection(entry.getDate());
                }

                // TODO: Could irritate user?
                // goToDay(entry.getDate());
            }
        }
    }

    @SuppressWarnings("unused")
    public void onEvent(Event.EntryUpdatedEvent event) {
        if (isAdded()) {
            Entry entry = event.context;
            if (entry != null) {
                int entryPosition = listAdapter.getEntryPosition(entry);
                if (entryPosition >= 0) {
                    ListItem listItem = listAdapter.getItem(entryPosition);
                    if (listItem instanceof ListItemEntry) {
                        ListItemEntry listItemEntry = (ListItemEntry) listItem;
                        entry.setMeasurementCache(EntryDao.getInstance().getMeasurements(entry));
                        listItemEntry.setEntry(event.context);
                        listAdapter.notifyItemChanged(entryPosition);
                    }
                }
            }
        }
    }

    @SuppressWarnings("unused")
    public void onEvent(Event.EntryDeletedEvent event) {
        if (isAdded()) {
            Entry entry = event.context;
            if (entry != null) {
                int entryPosition = listAdapter.getEntryPosition(entry);
                if (entryPosition >= 0) {
                    listAdapter.removeItem(entryPosition);
                    listAdapter.notifyItemRemoved(entryPosition);

                    // Add empty view if there is no entry available anymore for this day
                    DateTime day = entry.getDate();
                    boolean hasNoMoreEntries = listAdapter.getFirstListItemEntryOfDayPosition(day) == -1;
                    if (hasNoMoreEntries) {
                        listAdapter.addItem(entryPosition, new ListItemEmpty(day));
                        listAdapter.notifyItemInserted(entryPosition);
                    }

                    listDecoration.clearHeaderCache();
                }
            }
        }
    }
}
