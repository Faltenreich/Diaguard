package com.faltenreich.diaguard.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ProgressBar;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.ListSwipeHelper;
import com.faltenreich.diaguard.adapter.LogRecyclerAdapter;
import com.faltenreich.diaguard.adapter.SafeLinearLayoutManager;
import com.faltenreich.diaguard.adapter.StickyHeaderDecoration;
import com.faltenreich.diaguard.adapter.list.ListItem;
import com.faltenreich.diaguard.adapter.list.ListItemDate;
import com.faltenreich.diaguard.adapter.list.ListItemEmpty;
import com.faltenreich.diaguard.adapter.list.ListItemEntry;
import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.event.data.EntryAddedEvent;
import com.faltenreich.diaguard.event.data.EntryDeletedEvent;
import com.faltenreich.diaguard.event.data.EntryUpdatedEvent;
import com.faltenreich.diaguard.util.ViewUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;

import butterknife.BindView;

/**
 * Created by Filip on 05.07.2015.
 */
public class LogFragment extends DateFragment implements LogRecyclerAdapter.OnAdapterChangesListener {

    @BindView(R.id.fragment_log_list) RecyclerView recyclerView;
    @BindView(R.id.fragment_log_progressbar) ProgressBar progressBar;

    private LogRecyclerAdapter listAdapter;
    private StickyHeaderDecoration listDecoration;
    private LinearLayoutManager listLayoutManager;

    public LogFragment() {
        super(R.layout.fragment_log, R.string.log);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listLayoutManager = new SafeLinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(listLayoutManager);
        listAdapter = new LogRecyclerAdapter(getActivity(), this);
        recyclerView.setAdapter(listAdapter);
        listDecoration = new StickyHeaderDecoration(listAdapter, true);
        recyclerView.addItemDecoration(listDecoration);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ListSwipeHelper(listAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        // Fragment updates
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                DateTime firstVisibleDay = getFirstVisibleDay();
                if (firstVisibleDay != null) {
                    setDay(firstVisibleDay);
                    // Update month in Toolbar when section is being crossed
                    boolean isScrollingUp = dy < 0;
                    boolean isCrossingMonth = isScrollingUp ?
                            getDay().dayOfMonth().get() == getDay().dayOfMonth().getMaximumValue() :
                            getDay().dayOfMonth().get() == getDay().dayOfMonth().getMinimumValue();
                    if (isCrossingMonth) {
                        updateLabels();
                    }
                }
            }
        });

        progressBar.setVisibility(View.VISIBLE);
        goToDay(getDay());
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

    @Override
    protected void goToDay(DateTime dateTime) {
        super.goToDay(dateTime);

        int position = listAdapter.getDayPosition(dateTime);
        if (position >= 0) {
            recyclerView.scrollToPosition(position);
        } else {
            progressBar.setVisibility(View.VISIBLE);
            listAdapter.setup(dateTime);
        }
    }

    private void goToEntry(Entry entry) {
        super.goToDay(entry.getDate());

        int position = listAdapter.getEntryPosition(entry);
        if (position >= 0) {
            recyclerView.scrollToPosition(position);
        } else {
            // TODO: Remember entry to select afterwards
            progressBar.setVisibility(View.VISIBLE);
            listAdapter.setup(entry.getDate());
        }
    }

    @Override
    protected void updateLabels() {
        if (getActionView() != null) {
            String format;
            if (ViewUtils.isLandscape(getActivity()) || ViewUtils.isLargeScreen(getActivity())) {
                format = "MMMM YYYY";
            } else {
                format = "MMM YYYY";
            }
            getActionView().setText(getDay().toString(format));
        }
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

    private void addEntry(Entry entry) {
        if (entry != null) {
            DateTime date = entry.getDate();
            int position = listAdapter.getNextDateTimePosition(date);
            if (position >= 0) {

                // Remove any existing empty view
                int previousPosition = position - 1;
                ListItemDate previousListItem = listAdapter.getItem(previousPosition);
                if (previousListItem instanceof ListItemEmpty && previousListItem.getDateTime().getDayOfYear() == date.getDayOfYear()) {
                    listAdapter.removeItem(previousPosition);
                    listAdapter.notifyItemRemoved(previousPosition);
                    position = previousPosition;
                }

                entry.setMeasurementCache(EntryDao.getInstance().getMeasurements(entry));
                ListItemEntry listItemEntry = new ListItemEntry(entry);
                listAdapter.addItem(position, listItemEntry);
                listAdapter.notifyItemInserted(position);

                updateHeaderSection(date);
            }
        }
    }

    private void removeEntry(Entry entry) {
        if (entry != null) {
            int position = listAdapter.getEntryPosition(entry);
            if (position >= 0) {
                removeEntry(position, entry.getDate());
            }
        }
    }

    private void removeEntry(int position, DateTime date) {
        listAdapter.removeItem(position);

        // Add empty view if there is no entry available anymore for this day
        boolean hasNoMoreEntries = listAdapter.getFirstListItemEntryOfDayPosition(date) == -1;
        if (hasNoMoreEntries) {
            listAdapter.addItem(position, new ListItemEmpty(date));
            listAdapter.notifyItemChanged(position);
        } else {
            listAdapter.notifyItemRemoved(position);
        }

        updateHeaderSection(date);
    }

    private void updateEntry(Entry entry, DateTime originalDate) {
        if (entry != null) {
            int originalPosition = listAdapter.getEntryPosition(entry);
            if (originalPosition >= 0) {
                int updatedPosition = listAdapter.getNextDateTimePosition(entry.getDate()) - 1;
                if (originalPosition == updatedPosition) {
                    ListItem listItem = listAdapter.getItem(originalPosition);
                    if (listItem instanceof ListItemEntry) {
                        ListItemEntry listItemEntry = (ListItemEntry) listItem;
                        entry.setMeasurementCache(EntryDao.getInstance().getMeasurements(entry));
                        listItemEntry.setEntry(entry);
                        listAdapter.notifyItemChanged(originalPosition);
                    }
                } else {
                    removeEntry(originalPosition, originalDate);
                    addEntry(entry);
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EntryAddedEvent event) {
        if (isAdded()) {
            addEntry(event.context);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EntryDeletedEvent event) {
        super.onEvent(event);
        if (isAdded()) {
            removeEntry(event.context);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EntryUpdatedEvent event) {
        if (isAdded()) {
            updateEntry(event.context, event.originalDate);
        }
    }
}
