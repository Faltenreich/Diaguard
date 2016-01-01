package com.faltenreich.diaguard.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.faltenreich.diaguard.adapter.list.ListItemDate;
import com.faltenreich.diaguard.adapter.list.ListItemDay;
import com.faltenreich.diaguard.adapter.list.ListItemEmpty;
import com.faltenreich.diaguard.adapter.list.ListItemEntry;
import com.faltenreich.diaguard.adapter.list.ListItemMonth;
import com.faltenreich.diaguard.adapter.list.ListItemPending;
import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.ui.view.viewholder.BaseViewHolder;
import com.faltenreich.diaguard.ui.view.viewholder.LogDayViewHolder;
import com.faltenreich.diaguard.ui.view.viewholder.LogEmptyViewHolder;
import com.faltenreich.diaguard.ui.view.viewholder.LogEntryViewHolder;
import com.faltenreich.diaguard.ui.view.viewholder.LogMonthViewHolder;
import com.faltenreich.diaguard.ui.view.viewholder.LogPendingViewHolder;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Filip on 04.11.13.
 */
public class LogRecyclerAdapter extends EndlessAdapter<ListItemDate, BaseViewHolder<ListItemDate>>
        implements EndlessAdapter.OnEndlessListener, StickyHeaderAdapter<LogDayViewHolder> {

    private enum ViewType {
        MONTH,
        DAY,
        ENTRY,
        MEASUREMENT,
        EMPTY,
        PENDING
    }

    private OnAdapterChangesListener listener;

    private boolean shouldLoadPrevious;
    private boolean isLoadingPrevious;

    private boolean shouldLoadNext;
    private boolean isLoadingNext;

    public LogRecyclerAdapter(Context context, OnAdapterChangesListener listener) {
        super(context);
        this.listener = listener;
    }

    public void setup(DateTime startDateTime) {
        removeOnEndlessListener();

        int count = getItemCount();
        if (count > 0) {
            clear();
            notifyItemRangeRemoved(0, count - 1);
        }

        new SetupTask(startDateTime).execute();
    }

    /**
     * @return Position of the first ListItemEntry with the same day
     */
    public int getFirstListItemEntryOfDayPosition(DateTime day) {
        int firstListItemEntryOfDayPosition = -1;
        for (int position = getItemCount() - 1; position >= 0; position--) {
            ListItemDate listItem = getItem(position);

            boolean isSameDay = listItem.getDateTime().withTimeAtStartOfDay().equals(day.withTimeAtStartOfDay());
            if (isSameDay && listItem instanceof ListItemEntry) {
                firstListItemEntryOfDayPosition = position;
            }

            boolean isBefore = listItem.getDateTime().withTimeAtStartOfDay().isBefore(day.withTimeAtStartOfDay());
            if (isBefore) {
                break;
            }
        }
        return firstListItemEntryOfDayPosition;
    }

    /**
     * @return Position of the first ListItem with a higher date time
     */
    public int getNextDateTimePosition(DateTime dateTime) {
        for (int position = 0; position < getItemCount(); position++) {
            ListItemDate listItem = getItem(position);
            if (listItem.getDateTime().isAfter(dateTime)) {
                return position;
            }
        }
        return getItemCount() - 1;
    }

    public int getDayPosition(DateTime dateTime) {
        for (int position = getItemCount() - 1; position >= 0; position--) {
            ListItemDate listItem = getItem(position);
            boolean isSameDay = listItem.getDateTime().withTimeAtStartOfDay().isEqual(dateTime.withTimeAtStartOfDay());
            if (isSameDay) {
                return position;
            }
        }
        return -1;
    }

    public int getEntryPosition(Entry entry) {
        for (int position = 0; position < getItemCount(); position++) {
            ListItemDate listItem = getItem(position);
            if (listItem instanceof  ListItemEntry) {
                ListItemEntry listItemEntry = (ListItemEntry) listItem;
                if (listItemEntry.getEntry().equals(entry)) {
                    return position;
                }
            }
        }
        return -1;
    }

    @Override
    public void onLoadMore(Direction direction) {
        switch (direction) {
            case DOWN:
                appendNext();
                break;
            case UP:
                appendPrevious();
                break;
        }
    }

    private void appendNext() {
        if (!isLoadingNext) {
            if (isLoadingPrevious) {
                shouldLoadNext = true;
            } else {
                isLoadingNext = true;
                new AppendNextTask().execute();
            }
        }
    }

    private void appendPrevious() {
        if (!isLoadingPrevious) {
            if (isLoadingNext) {
                shouldLoadPrevious = true;
            } else {
                isLoadingPrevious = true;
                new AppendPreviousTask().execute();
            }
        }
    }

    private void removePendingView(Direction direction) {
        if (getItemCount() > 0) {
            int position = -1;
            switch (direction) {
                case DOWN:
                    if (getItem(getItemCount() - 1) instanceof ListItemPending) {
                        position = getItemCount() - 1;
                    }
                    break;
                case UP:
                    if (getItem(0) instanceof ListItemPending) {
                        position = 0;
                    }
                    break;
            }
            if (position >= 0) {
                removeItem(position);
            }
        }
    }

    private List<Entry> fetchData(DateTime day) {
        List<Entry> entriesOfDay = EntryDao.getInstance().getEntriesOfDay(day);
        // TODO: Get rid of measurementCache (Currently required to enable lazy loading for generic measurements
        for (Entry entry : entriesOfDay) {
            List<Measurement> measurements = EntryDao.getInstance().getMeasurements(entry);
            entry.setMeasurementCache(measurements);
        }
        return entriesOfDay;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItemCount() == 0) {
            return ViewType.ENTRY.ordinal();
        } else {
            ListItemDate item = getItem(position);
            if (item instanceof ListItemMonth) {
                return ViewType.MONTH.ordinal();
            } else if (item instanceof ListItemEntry) {
                return ViewType.ENTRY.ordinal();
            } else if (item instanceof ListItemEmpty) {
                return ViewType.EMPTY.ordinal();
            } else if (item instanceof ListItemPending) {
                return ViewType.PENDING.ordinal();
            }
        }
        return super.getItemViewType(position);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewTypeInt) {
        ViewType viewType = ViewType.values()[viewTypeInt];
        switch (viewType) {
            case MONTH:
                return new LogMonthViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.list_item_log_month, parent, false));
            case ENTRY:
                return new LogEntryViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.list_item_log_entry, parent, false));
            case EMPTY:
                return new LogEmptyViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.list_item_log_empty, parent, false));
            case PENDING:
                return new LogPendingViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.list_item_log_pending, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.bindData(getItem(position));
    }

    @Override
    public void onViewRecycled(BaseViewHolder holder) {
        if (holder instanceof LogEntryViewHolder) {
            ((LogEntryViewHolder)holder).measurements.removeAllViews();
        }
        super.onViewRecycled(holder);
    }

    @Override
    public long getHeaderId(int position) {
        if (position >= 0 && position < getItemCount()) {
            ListItemDate listItem = getItem(position);
            if (listItem instanceof ListItemEntry) {
                return getItemPosition(((ListItemEntry) listItem).getFirstListItemEntryOfDay());
            }
        }
        return position;
    }

    @Override
    public LogDayViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return new LogDayViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.list_item_log_day, parent, false));
    }

    @Override
    public void onBindHeaderViewHolder(LogDayViewHolder holder, int position) {
        if (position >= 0 && position < getItemCount()) {
            ListItemDate listItem = getItem(position);
            // TODO: Remove header for other items instead of ignoring them here
            if (listItem instanceof ListItemEntry || listItem instanceof ListItemEmpty) {
                holder.bindData(new ListItemDay(listItem.getDateTime()));
            }
        }
    }

    private class SetupTask extends AsyncTask<Void, Integer, List<ListItemDate>> {

        private DateTime startDate;

        public SetupTask(DateTime startDate) {
            this.startDate = startDate;
        }

        @Override
        protected List<ListItemDate> doInBackground(Void... params) {
            List<ListItemDate> listItems = new ArrayList<>();
            DateTime currentDate = startDate.minusDays(EndlessAdapter.BULK_SIZE);
            DateTime targetDate = startDate.plusDays(EndlessAdapter.BULK_SIZE);

            while (currentDate.isBefore(targetDate)) {
                boolean isFirstDayOfMonth = currentDate.dayOfMonth().get() == 1;
                if (isFirstDayOfMonth) {
                    listItems.add(new ListItemMonth(currentDate));
                }

                List<Entry> entries = fetchData(currentDate);
                if (entries.size() > 0) {
                    List<ListItemEntry> listItemEntries = new ArrayList<>();
                    for (Entry entry : entries) {
                        listItemEntries.add(new ListItemEntry(entry));
                    }
                    ListItemEntry firstListItemEntryOfDay = listItemEntries.get(listItemEntries.size() - 1);
                    for (ListItemEntry listItemEntry : listItemEntries) {
                        listItemEntry.setFirstListItemEntryOfDay(firstListItemEntryOfDay);
                        listItems.add(listItemEntry);
                    }
                } else {
                    listItems.add(new ListItemEmpty(currentDate));
                }

                currentDate = currentDate.plusDays(1);
            }

            return listItems;
        }

        @Override
        protected void onPostExecute(List<ListItemDate> listItems) {
            addItems(listItems);
            notifyItemRangeInserted(getItemCount() - listItems.size(), getItemCount() - 1);

            listener.onOrderChanges();
            listener.onSetupComplete(startDate);

            setOnEndlessListener(LogRecyclerAdapter.this);
        }
    }

    private class AppendPreviousTask extends AsyncTask<Void, Integer, List<ListItemDate>> {

        @Override
        protected List<ListItemDate> doInBackground(Void... params) {
            List<ListItemDate> listItems = new ArrayList<>();

            DateTime minVisibleDate = getItem(0).getDateTime();
            DateTime targetDate = minVisibleDate.minusDays(EndlessAdapter.BULK_SIZE);
            while (minVisibleDate.isAfter(targetDate)) {
                minVisibleDate = minVisibleDate.minusDays(1);

                List<Entry> entries = fetchData(minVisibleDate);
                if (entries.size() > 0) {
                    List<ListItemEntry> listItemEntries = new ArrayList<>();
                    for (Entry entry : entries) {
                        listItemEntries.add(new ListItemEntry(entry));
                    }
                    ListItemEntry firstListItemEntryOfDay = listItemEntries.get(listItemEntries.size() - 1);
                    for (ListItemEntry listItemEntry : listItemEntries) {
                        listItemEntry.setFirstListItemEntryOfDay(firstListItemEntryOfDay);
                        listItems.add(0, listItemEntry);
                    }
                } else {
                    listItems.add(0, new ListItemEmpty(minVisibleDate));
                }

                boolean isFirstDayOfMonth = minVisibleDate.dayOfMonth().get() == 1;
                if (isFirstDayOfMonth) {
                    listItems.add(0, new ListItemMonth(minVisibleDate));
                }
            }

            return listItems;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            DateTime dateTime = getItem(0).getDateTime();
            addItem(0, new ListItemPending(dateTime));
            notifyItemInserted(0);

            listener.onOrderChanges();
        }

        @Override
        protected void onPostExecute(List<ListItemDate> listItems) {
            removePendingView(Direction.UP);

            addItems(0, listItems);
            notifyItemRangeInserted(0, listItems.size() - 1);

            listener.onOrderChanges();

            isLoadingPrevious = false;
            if (shouldLoadNext) {
                shouldLoadNext = false;
                appendNext();
            }
        }
    }

    private class AppendNextTask extends AsyncTask<Void, Integer, List<ListItemDate>> {

        @Override
        protected List<ListItemDate> doInBackground(Void... params) {
            List<ListItemDate> listItems = new ArrayList<>();

            DateTime maxVisibleDate = getItem(getItemCount() - 1).getDateTime();
            DateTime targetDate = maxVisibleDate.plusDays(EndlessAdapter.BULK_SIZE);
            while (maxVisibleDate.isBefore(targetDate)) {
                maxVisibleDate = maxVisibleDate.plusDays(1);

                boolean isFirstDayOfMonth = maxVisibleDate.dayOfMonth().get() == 1;
                if (isFirstDayOfMonth) {
                    listItems.add(new ListItemMonth(maxVisibleDate));
                }

                List<Entry> entries = fetchData(maxVisibleDate);
                if (entries.size() > 0) {
                    List<ListItemEntry> listItemEntries = new ArrayList<>();
                    for (Entry entry : entries) {
                        listItemEntries.add(new ListItemEntry(entry));
                    }
                    ListItemEntry firstListItemEntryOfDay = listItemEntries.get(0);
                    for (ListItemEntry listItemEntry : listItemEntries) {
                        listItemEntry.setFirstListItemEntryOfDay(firstListItemEntryOfDay);
                        listItems.add(listItemEntry);
                    }
                } else {
                    listItems.add(new ListItemEmpty(maxVisibleDate));
                }
            }

            return listItems;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            DateTime dateTime = getItem(getItemCount() - 1).getDateTime();
            addItem(getItemCount(), new ListItemPending(dateTime));
            notifyItemInserted(getItemCount());
        }

        @Override
        protected void onPostExecute(List<ListItemDate> listItems) {
            removePendingView(Direction.DOWN);

            addItems(listItems);
            notifyItemRangeInserted(getItemCount() - listItems.size(), getItemCount() - 1);
            isLoadingNext = false;

            if (shouldLoadPrevious) {
                shouldLoadPrevious = false;
                appendPrevious();
            }
        }
    }

    // Required to notify position changes
    public interface OnAdapterChangesListener {
        void onOrderChanges();
        void onSetupComplete(DateTime dateTime);
    }
}