package com.faltenreich.diaguard.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.LogRecyclerAdapter;
import com.faltenreich.diaguard.adapter.SafeLinearLayoutManager;
import com.faltenreich.diaguard.adapter.StickyHeaderDecoration;
import com.faltenreich.diaguard.adapter.list.ListItem;
import com.faltenreich.diaguard.adapter.list.ListItemDate;
import com.faltenreich.diaguard.adapter.list.ListItemEmpty;
import com.faltenreich.diaguard.adapter.list.ListItemEntry;
import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.util.ViewHelper;
import com.faltenreich.diaguard.util.event.data.EntryAddedEvent;
import com.faltenreich.diaguard.util.event.data.EntryDeletedEvent;
import com.faltenreich.diaguard.util.event.data.EntryUpdatedEvent;

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
        super(R.layout.fragment_log);
    }

    @Override
    public String getTitle() {
        return DiaguardApplication.getContext().getString(R.string.log);
    }

    protected void initialize() {
        super.initialize();

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

                setDay(getFirstVisibleDay());
                // Update month in Toolbar when section is being crossed
                boolean isScrollingUp = dy < 0;
                boolean isCrossingMonth = isScrollingUp ?
                        getDay().dayOfMonth().get() == getDay().dayOfMonth().getMaximumValue() :
                        getDay().dayOfMonth().get() == getDay().dayOfMonth().getMinimumValue();
                if (isCrossingMonth) {
                    updateLabels();
                }
            }
        });

        progressBar.setVisibility(View.VISIBLE);
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

        int positionOfDay = listAdapter.getDayPosition(dateTime);
        boolean containsDay = positionOfDay >= 0;
        if (containsDay) {
            recyclerView.scrollToPosition(positionOfDay);
        } else {
            progressBar.setVisibility(View.VISIBLE);
            listAdapter.setup(dateTime);
        }
    }

    @Override
    protected void updateLabels() {
        String format;
        if (ViewHelper.isLandscape(getActivity()) || ViewHelper.isLargeScreen(getActivity())) {
            format = "MMMM YYYY";
        } else {
            format = "MMM YYYY";
        }
        getActionView().setText(getDay().toString(format));
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
    public void onEvent(EntryAddedEvent event) {
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
    public void onEvent(EntryUpdatedEvent event) {
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

    @Override
    public void onEvent(EntryDeletedEvent event) {
        super.onEvent(event);

        if (isAdded()) {
            final Entry entry = event.context;
            if (entry != null) {
                // Remove from list
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
