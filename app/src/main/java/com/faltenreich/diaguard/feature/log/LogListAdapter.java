package com.faltenreich.diaguard.feature.log;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.shared.data.database.entity.Entry;
import com.faltenreich.diaguard.feature.entry.search.EntrySearchListAdapter;
import com.faltenreich.diaguard.feature.log.day.LogDayListItem;
import com.faltenreich.diaguard.feature.log.day.LogDayViewHolder;
import com.faltenreich.diaguard.feature.log.empty.LogEmptyListItem;
import com.faltenreich.diaguard.feature.log.empty.LogEmptyViewHolder;
import com.faltenreich.diaguard.feature.log.entry.LogEntryListItem;
import com.faltenreich.diaguard.feature.log.entry.LogEntryViewHolder;
import com.faltenreich.diaguard.feature.log.month.LogMonthListItem;
import com.faltenreich.diaguard.feature.log.month.LogMonthViewHolder;
import com.faltenreich.diaguard.feature.log.pending.LogPendingListItem;
import com.faltenreich.diaguard.feature.log.pending.LogPendingViewHolder;
import com.faltenreich.diaguard.shared.view.recyclerview.adapter.EndlessAdapter;
import com.faltenreich.diaguard.shared.view.recyclerview.adapter.StickyHeaderAdapter;
import com.faltenreich.diaguard.shared.view.recyclerview.viewholder.BaseViewHolder;

import org.joda.time.DateTime;

class LogListAdapter
    extends EndlessAdapter<LogListItem, BaseViewHolder<LogListItem>>
    implements EndlessAdapter.OnEndlessListener, StickyHeaderAdapter<LogDayViewHolder> {

    private enum ViewType {
        MONTH,
        DAY,
        ENTRY,
        MEASUREMENT,
        EMPTY,
        PENDING
    }

    private Listener listener;

    private boolean isInitializing;
    private boolean isLoadingPrevious;
    private boolean isLoadingNext;
    private boolean shouldLoadPrevious;
    private boolean shouldLoadNext;

    LogListAdapter(Context context, Listener listener) {
        super(context);
        this.listener = listener;
    }

    void setup(DateTime startDate) {
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
    int getFirstListItemEntryOfDayPosition(DateTime day) {
        int firstListItemEntryOfDayPosition = -1;
        for (int position = getItemCount() - 1; position >= 0; position--) {
            LogListItem listItem = getItem(position);

            boolean isSameDay = listItem.getDateTime().withTimeAtStartOfDay().equals(day.withTimeAtStartOfDay());
            if (isSameDay && listItem instanceof LogEntryListItem) {
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
    int getNextDateTimePosition(DateTime dateTime) {
        if (getItemCount() > 0) {
            DateTime visibleMinDate = getItem(0).getDateTime().withTimeAtStartOfDay();
            DateTime visibleMaxDate = getItem(getItemCount() - 1).getDateTime().withTimeAtStartOfDay();
            boolean isInRange = visibleMinDate.isBefore(dateTime) && visibleMaxDate.isAfter(dateTime);
            if (isInRange) {
                for (int position = 0; position < getItemCount(); position++) {
                    LogListItem listItem = getItem(position);
                    if (listItem.getDateTime().isAfter(dateTime)) {
                        return position;
                    }
                }
            }
        }
        return -1;
    }

    int getDayPosition(DateTime dateTime) {
        for (int position = getItemCount() - 1; position >= 0; position--) {
            LogListItem listItem = getItem(position);
            boolean isSameDay = listItem.getDateTime().withTimeAtStartOfDay().isEqual(dateTime.withTimeAtStartOfDay());
            if (isSameDay) {
                return position;
            }
        }
        return -1;
    }

    int getEntryPosition(Entry entry) {
        for (int position = 0; position < getItemCount(); position++) {
            LogListItem listItem = getItem(position);
            if (listItem instanceof LogEntryListItem) {
                LogEntryListItem listItemEntry = (LogEntryListItem) listItem;
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
            LogListItem listItem = getItem(position);
            if (!(listItem instanceof LogPendingListItem)) {
                position = scrollingDown ? position + 1 : position;
                addItem(position, new LogPendingListItem(listItem.getDateTime()));
                notifyItemInserted(position);
            }
        }
    }

    private void removePendingView(boolean scrollingDown) {
        if (getItemCount() > 0) {
            int position = scrollingDown ? getItemCount() - 1 : 0;
            LogListItem listItem = getItem(position);
            if (listItem instanceof LogPendingListItem) {
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
            LogListItem item = getItem(position);
            if (item instanceof LogMonthListItem) {
                return ViewType.MONTH.ordinal();
            } else if (item instanceof LogEntryListItem) {
                return ViewType.ENTRY.ordinal();
            } else if (item instanceof LogEmptyListItem) {
                return ViewType.EMPTY.ordinal();
            } else if (item instanceof LogPendingListItem) {
                return ViewType.PENDING.ordinal();
            }
        }
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewTypeInt) {
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
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.bindData(getItem(position));
    }

    @Override
    public void onViewRecycled(@NonNull BaseViewHolder holder) {
        if (holder instanceof LogEntryViewHolder) {
            ((LogEntryViewHolder)holder).measurementsLayout.removeAllViews();
        }
        super.onViewRecycled(holder);
    }

    @Override
    public long getHeaderId(int position) {
        if (position >= 0 && position < getItemCount()) {
            LogListItem listItem = getItem(position);
            if (listItem instanceof LogEntryListItem) {
                return getItemPosition(((LogEntryListItem) listItem).getFirstListItemEntryOfDay());
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
            LogListItem listItem = getItem(position);
            // TODO: Remove header for other items instead of ignoring them here
            if (listItem instanceof LogEntryListItem || listItem instanceof LogEmptyListItem) {
                holder.bindData(new LogDayListItem(listItem.getDateTime()));
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

        new LogFetchDataTask(date, scrollingDown, isInitializing, (result, scrollingDown1) -> {
            removePendingView(scrollingDown1);

            int itemCount = getItemCount();
            int index = scrollingDown1 ? itemCount : 0;
            addItems(index, result);
            notifyItemRangeInserted(index, result.size());
            itemCount += result.size();

            listener.onOrderChanges();

            if (scrollingDown1) {
                isLoadingNext = false;
            } else {
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
                setOnEndlessListener(LogListAdapter.this);
                listener.onSetupEnd();
            }
        }).execute();
    }

    interface Listener extends EntrySearchListAdapter.OnSearchItemClickListener {
        void onOrderChanges();
        void onSetupEnd();
    }
}