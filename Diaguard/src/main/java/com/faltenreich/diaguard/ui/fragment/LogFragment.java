package com.faltenreich.diaguard.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.View;
import android.widget.ProgressBar;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.ui.list.helper.ListSwipeHelper;
import com.faltenreich.diaguard.ui.list.adapter.LogRecyclerAdapter;
import com.faltenreich.diaguard.ui.list.layoutmanager.SafeLinearLayoutManager;
import com.faltenreich.diaguard.ui.list.decoration.StickyHeaderDecoration;
import com.faltenreich.diaguard.ui.list.item.ListItem;
import com.faltenreich.diaguard.ui.list.item.ListItemDate;
import com.faltenreich.diaguard.ui.list.item.ListItemEmpty;
import com.faltenreich.diaguard.ui.list.item.ListItemEntry;
import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.EntryTag;
import com.faltenreich.diaguard.data.entity.FoodEaten;
import com.faltenreich.diaguard.data.entity.Tag;
import com.faltenreich.diaguard.data.event.BackupImportedEvent;
import com.faltenreich.diaguard.data.event.data.EntryAddedEvent;
import com.faltenreich.diaguard.data.event.data.EntryDeletedEvent;
import com.faltenreich.diaguard.data.event.data.EntryUpdatedEvent;
import com.faltenreich.diaguard.data.event.preference.UnitChangedEvent;
import com.faltenreich.diaguard.ui.activity.EntrySearchActivity;
import com.faltenreich.diaguard.util.ViewUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Filip on 05.07.2015.
 */
public class LogFragment extends DateFragment implements LogRecyclerAdapter.LogListListener {

    @BindView(R.id.log_list) RecyclerView recyclerView;
    @BindView(R.id.log_progressbar) ProgressBar progressBar;

    private LogRecyclerAdapter listAdapter;
    private StickyHeaderDecoration listDecoration;
    private LinearLayoutManager listLayoutManager;

    public LogFragment() {
        super(R.layout.fragment_log, R.string.log);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLayout();

        // Fake delay for smoother fragment transitions
        new Handler().postDelayed(() -> goToDay(getDay()), 350);
    }

    @Override
    public String getTitle() {
        String format = ViewUtils.isLandscape(getActivity()) || ViewUtils.isLargeScreen(getActivity()) ? "MMMM YYYY" : "MMM YYYY";
        return getDay().toString(format);
    }

    private void initLayout() {
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
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
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

    @Override
    public void onOrderChanges() {
        invalidateSections();
    }

    @Override
    public void onSetupEnd() {
        progressBar.setVisibility(View.GONE);
        goToDay(getDay());
    }

    @Override
    public void onTagClicked(Tag tag, View view) {
        if (isAdded()) {
            EntrySearchActivity.show(getContext(), tag);
        }
    }

    private void invalidateSections() {
        if (isAdded() && listDecoration != null) {
            listDecoration.clearHeaderCache();
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
        invalidateSections();
    }

    private void addEntry(Entry entry, List<EntryTag> entryTags, List<FoodEaten> foodEatenList) {
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

                ListItemEntry listItemEntry = new ListItemEntry(entry, entryTags, foodEatenList);
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

    private void updateEntry(Entry entry, List<EntryTag> entryTags, List<FoodEaten> foodEatenList, DateTime originalDate) {
        if (entry != null) {
            int originalPosition = listAdapter.getEntryPosition(entry);
            if (originalPosition >= 0) {
                int updatedPosition = listAdapter.getNextDateTimePosition(entry.getDate()) - 1;
                if (originalPosition == updatedPosition) {
                    ListItem listItem = listAdapter.getItem(originalPosition);
                    if (listItem instanceof ListItemEntry) {
                        ListItemEntry listItemEntry = (ListItemEntry) listItem;
                        listItemEntry.setEntry(entry);
                        listItemEntry.setEntryTags(entryTags);
                        listItemEntry.setFoodEatenList(foodEatenList);
                        listAdapter.notifyItemChanged(originalPosition);
                    }
                } else {
                    removeEntry(originalPosition, originalDate);
                    addEntry(entry, entryTags, foodEatenList);
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EntryAddedEvent event) {
        if (isAdded()) {
            addEntry(event.context, event.entryTags, event.foodEatenList);
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
            updateEntry(event.context, event.entryTags, event.foodEatenList, event.originalDate);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(@SuppressWarnings("unused") UnitChangedEvent event) {
        if (isAdded()) {
            progressBar.setVisibility(View.VISIBLE);
            listAdapter.setup(getDay());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(@SuppressWarnings("unused") BackupImportedEvent event) {
        if (isAdded()) {
            progressBar.setVisibility(View.VISIBLE);
            listAdapter.setup(getDay());
        }
    }
}
