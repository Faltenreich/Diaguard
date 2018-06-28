package com.faltenreich.diaguard.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.list.ListItemDate;
import com.faltenreich.diaguard.adapter.list.ListItemDay;
import com.faltenreich.diaguard.adapter.list.ListItemEmpty;
import com.faltenreich.diaguard.adapter.list.ListItemEntry;
import com.faltenreich.diaguard.adapter.list.ListItemMonth;
import com.faltenreich.diaguard.adapter.list.ListItemPending;
import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.dao.EntryTagDao;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.EntryTag;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.ui.view.viewholder.BaseViewHolder;
import com.faltenreich.diaguard.ui.view.viewholder.LogDayViewHolder;
import com.faltenreich.diaguard.ui.view.viewholder.LogEmptyViewHolder;
import com.faltenreich.diaguard.ui.view.viewholder.LogEntryViewHolder;
import com.faltenreich.diaguard.ui.view.viewholder.LogMonthViewHolder;
import com.faltenreich.diaguard.ui.view.viewholder.LogPendingViewHolder;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class LogRecyclerAdapter extends EndlessAdapter<ListItemDate, BaseViewHolder<ListItemDate>> implements EndlessAdapter.OnEndlessListener, StickyHeaderAdapter<LogDayViewHolder> {

    private enum ViewType {
        MONTH,
        DAY,
        ENTRY,
        MEASUREMENT,
        EMPTY,
        PENDING
    }

    private LogListListener listener;

    private boolean isLoadingPrevious;
    private boolean isLoadingNext;
    private boolean shouldLoadPrevious;
    private boolean shouldLoadNext;
    private boolean isInitializing;

    public LogRecyclerAdapter(Context context, LogListListener listener) {
        super(context);
        this.listener = listener;
    }

    public void setup(DateTime startDate) {
        int count = getItemCount();
        if (count > 0) {
            clear();
            notifyItemRangeRemoved(0, count);
        }

        isInitializing = true;
        fetchData(startDate, false);
    }

    /**
     * Required for StickyHeaderItemDecoration
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
     * Required for notifying changes
     * @return Position of the first ListItem with a higher date time
     */
    public int getNextDateTimePosition(DateTime dateTime) {
        if (getItemCount() > 0) {
            DateTime visibleMinDate = getItem(0).getDateTime().withTimeAtStartOfDay();
            DateTime visibleMaxDate = getItem(getItemCount() - 1).getDateTime().withTimeAtStartOfDay();
            boolean isInRange = visibleMinDate.isBefore(dateTime) && visibleMaxDate.isAfter(dateTime);
            if (isInRange) {
                for (int position = 0; position < getItemCount(); position++) {
                    ListItemDate listItem = getItem(position);
                    if (listItem.getDateTime().isAfter(dateTime)) {
                        return position;
                    }
                }
            }
        }
        return -1;
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

    private void addPendingView(boolean scrollingDown) {
        if (getItemCount() > 0) {
            int position = scrollingDown ? getItemCount() - 1 : 0;
            ListItemDate listItem = getItem(position);
            if (!(listItem instanceof ListItemPending)) {
                position = scrollingDown ? position + 1 : position;
                addItem(position, new ListItemPending(listItem.getDateTime()));
                notifyItemInserted(position);
            }
        }
    }

    private void removePendingView(boolean scrollingDown) {
        if (getItemCount() > 0) {
            int position = scrollingDown ? getItemCount() - 1 : 0;
            ListItemDate listItem = getItem(position);
            if (listItem instanceof ListItemPending) {
                removeItem(position);
                notifyItemRemoved(position);
            }
        }
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
                return new LogEntryViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.list_item_log_entry, parent, false), listener);
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

    @Override
    public void onLoadMore(boolean scrollingDown) {
        if (isLoadingPrevious || isLoadingNext) {
            if (scrollingDown) {
                shouldLoadNext = true;
            } else {
                shouldLoadPrevious = true;
            }
        } else {
            DateTime date = getItem(scrollingDown ? getItemCount() - 1 : 0).getDateTime();
            date = scrollingDown ? date.plusDays(1) : date.minusDays(1);
            fetchData(date, scrollingDown);
        }
    }

    private void fetchData(DateTime date, boolean scrollingDown) {
        addPendingView(scrollingDown);

        if (scrollingDown) {
            isLoadingNext = true;
        } else {
            isLoadingPrevious = true;
        }

        new FetchDataTask(date, scrollingDown, isInitializing, new OnAsyncTaskListener<List<ListItemDate>>() {
            @Override
            public void onFinish(List<ListItemDate> result, boolean scrollingDown) {
                removePendingView(scrollingDown);

                int itemCount = getItemCount();
                int index = scrollingDown ? itemCount : 0;
                addItems(index, result);
                notifyItemRangeInserted(index, result.size());
                itemCount += result.size();

                listener.onOrderChanges();

                if (scrollingDown) {
                    isLoadingNext = false;
                } else {
                    notifyItemRangeChanged(0, result.size());
                    isLoadingPrevious = false;
                }

                if (shouldLoadPrevious) {
                    shouldLoadPrevious = false;
                    fetchData(getItem(0).getDateTime().minusDays(1), false);

                } else if (shouldLoadNext) {
                    shouldLoadNext = false;
                    fetchData(getItem(itemCount - 1).getDateTime().plusDays(1), true);

                } else if (isInitializing) {
                    isInitializing = false;
                    setOnEndlessListener(LogRecyclerAdapter.this);
                    listener.onSetupEnd();
                }
            }
        }).execute();
    }

    private static class FetchDataTask extends AsyncTask<Void, Integer, List<ListItemDate>> {

        private DateTime startDate;
        private boolean scrollingDown;
        private boolean isInitializing;
        private OnAsyncTaskListener<List<ListItemDate>> listener;

        FetchDataTask(DateTime startDate, boolean scrollingDown, boolean isInitializing, OnAsyncTaskListener<List<ListItemDate>> listener) {
            this.startDate = startDate;
            this.scrollingDown = scrollingDown;
            this.isInitializing = isInitializing;
            this.listener = listener;
        }

        @Override
        protected List<ListItemDate> doInBackground(Void... params) {
            List<ListItemDate> listItems = getListItems(startDate, scrollingDown);
            if (isInitializing) {
                boolean newIsScrollingDown = !scrollingDown;
                int index = newIsScrollingDown ? listItems.size() : 0;
                listItems.addAll(index, getListItems(newIsScrollingDown ? startDate.plusDays(1) : startDate.minusDays(1), newIsScrollingDown));
            }
            return listItems;
        }

        @Override
        protected void onPostExecute(List<ListItemDate> result) {
            listener.onFinish(result, scrollingDown);
        }

        private List<ListItemDate> getListItems(DateTime startDate, boolean scrollingDown) {
            List<ListItemDate> listItems = new ArrayList<>();
            DateTime date = startDate;
            boolean loadMore = true;

            while (loadMore) {
                List<Entry> entries = EntryDao.getInstance().getEntriesOfDay(date);
                for (Entry entry : entries) {
                    List<Measurement> measurements = EntryDao.getInstance().getMeasurements(entry);
                    entry.setMeasurementCache(measurements);
                }

                if (entries.size() > 0) {
                    ListItemEntry firstListItemEntryOfDay = null;

                    for (int entryIndex = 0; entryIndex < entries.size(); entryIndex++) {
                        Entry entry = entries.get(entryIndex);
                        List<EntryTag> entryTags = EntryTagDao.getInstance().getAll(entry);
                        ListItemEntry listItemEntry = new ListItemEntry(entry, entryTags);

                        if (entryIndex == 0) {
                            firstListItemEntryOfDay = listItemEntry;
                        }

                        listItemEntry.setFirstListItemEntryOfDay(firstListItemEntryOfDay);
                        listItems.add(scrollingDown ? listItems.size() : entryIndex, listItemEntry);
                    }

                } else {
                    listItems.add(scrollingDown ? listItems.size() : 0, new ListItemEmpty(date));
                }

                boolean isFirstDayOfMonth = date.dayOfMonth().get() == 1;
                if (isFirstDayOfMonth) {
                    listItems.add(scrollingDown ? listItems.size() - 1 : 0, new ListItemMonth(date));
                }

                loadMore = listItems.size() < (EndlessAdapter.BULK_SIZE);
                date = scrollingDown ? date.plusDays(1) : date.minusDays(1);
            }

            return listItems;
        }
    }

    public interface LogListListener extends SearchAdapter.OnSearchItemClickListener {
        void onOrderChanges();
        void onSetupEnd();
    }

    private interface OnAsyncTaskListener<T> {
        void onFinish(T result, boolean scrollingDown);
    }
}