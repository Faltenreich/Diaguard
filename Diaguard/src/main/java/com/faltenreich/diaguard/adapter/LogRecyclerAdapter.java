package com.faltenreich.diaguard.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.ui.viewholder.BaseViewHolder;
import com.faltenreich.diaguard.ui.viewholder.LogDayViewHolder;
import com.faltenreich.diaguard.ui.viewholder.LogEmptyViewHolder;
import com.faltenreich.diaguard.ui.viewholder.LogEntryViewHolder;
import com.faltenreich.diaguard.ui.viewholder.LogMonthViewHolder;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Filip on 04.11.13.
 */
public class LogRecyclerAdapter extends EndlessAdapter<ListItem, BaseViewHolder<ListItem>>
        implements EndlessAdapter.OnEndlessListener, StickyHeaderAdapter<LogDayViewHolder> {

    private enum ViewType {
        MONTH,
        DAY,
        ENTRY,
        MEASUREMENT,
        EMPTY
    }

    private DateTime maxVisibleDate;
    private DateTime minVisibleDate;

    private boolean shouldLoadPreviousMonth;
    private boolean isLoadingPreviousMonth;

    private boolean shouldLoadNextMonth;
    private boolean isLoadingNextMonth;

    public LogRecyclerAdapter(Context context, DateTime firstVisibleDay) {
        super(context);

        minVisibleDate = firstVisibleDay.withDayOfMonth(1);
        maxVisibleDate = minVisibleDate;

        appendNextMonth();

        setOnEndlessListener(this);

        // Workaround to endless scrolling when after visible threshold
        if (firstVisibleDay.dayOfMonth().get() >= (firstVisibleDay.dayOfMonth().getMaximumValue() - EndlessAdapter.VISIBLE_THRESHOLD) - 1) {
            appendNextMonth();
        }
    }

    @Override
    public void onLoadMore(Direction direction) {
        switch (direction) {
            case DOWN:
                appendNextMonth();
                break;
            case UP:
                appendPreviousMonth();
                break;
        }
    }

    private void appendNextMonth() {
        if (!isLoadingNextMonth) {
            if (isLoadingPreviousMonth) {
                shouldLoadNextMonth = true;
            } else {
                isLoadingNextMonth = true;
                new AppendNextMonthTask().execute();
            }
        }
    }

    private void appendPreviousMonth() {
        if (!isLoadingPreviousMonth) {
            if (isLoadingNextMonth) {
                shouldLoadPreviousMonth = true;
            } else {
                isLoadingPreviousMonth = true;
                new AppendPreviousMonthTask().execute();
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

    public int getDayPosition(DateTime dateTime) {
        for (int position = 0; position < getItemCount(); position++) {
            ListItem listItem = getItem(position);
            if (listItem instanceof ListItemDay) {
                ListItemDay listItemDay = (ListItemDay)listItem;
                boolean isSameDay = listItemDay.getDay().withTimeAtStartOfDay().equals(dateTime.withTimeAtStartOfDay());
                if (isSameDay) {
                    return position;
                }
            }
        }
        return -1;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItemCount() == 0) {
            return ViewType.ENTRY.ordinal();
        } else {
            ListItem item = getItem(position);
            if (item instanceof ListItemMonth) {
                return ViewType.MONTH.ordinal();
            } else if (item instanceof ListItemEntry) {
                return ViewType.ENTRY.ordinal();
            } else if (item instanceof ListItemEmpty) {
                return ViewType.EMPTY.ordinal();
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
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (holder != null) {
            ListItem listItem = getItem(position);
            holder.bindData(listItem);
        }
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
        ListItem listItem = getItem(position);
        if (listItem instanceof ListItemMonth) {
            return position;
        } else if (listItem instanceof ListItemDay) {
            return position;
        } else if (listItem instanceof ListItemEntry) {
            return getItemPosition(((ListItemEntry) listItem).getFirstListItemEntryOfDay());
        } else if (listItem instanceof ListItemEmpty) {
            return position;
        } else {
            return -1;
        }
    }

    @Override
    public LogDayViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return new LogDayViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.list_item_log_day, parent, false));
    }

    @Override
    public void onBindHeaderViewHolder(LogDayViewHolder holder, int position) {
        ListItem listItem = getItem(position);
        // TODO: Remove header for other items instead of ignoring them here
        if (listItem instanceof ListItemEntry || listItem instanceof ListItemEmpty) {
            holder.bindData(new ListItemDay(listItem.getDateTime()));
        }
    }

    private class AppendPreviousMonthTask extends AsyncTask<Void, Integer, List<ListItem>> {

        protected List<ListItem> doInBackground(Void... params) {
            List<ListItem> listItems = new ArrayList<>();
            DateTime targetDate = minVisibleDate.minusMonths(1);

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
            }

            listItems.add(0, new ListItemMonth(minVisibleDate));

            return listItems;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(List<ListItem> listItems) {
            addItems(0, listItems);
            notifyItemRangeInserted(0, listItems.size());

            isLoadingPreviousMonth = false;
            if (shouldLoadNextMonth) {
                shouldLoadNextMonth = false;
                appendNextMonth();
            }
        }
    }

    private class AppendNextMonthTask extends AsyncTask<Void, Integer, List<ListItem>> {

        protected List<ListItem> doInBackground(Void... params) {
            List<ListItem> listItems = new ArrayList<>();

            DateTime targetDate = maxVisibleDate.plusMonths(1);
            listItems.add(new ListItemMonth(maxVisibleDate));

            while (maxVisibleDate.isBefore(targetDate)) {
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
                maxVisibleDate = maxVisibleDate.plusDays(1);
            }
            return listItems;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(List<ListItem> listItems) {
            addItems(listItems);
            notifyItemRangeInserted(getItemCount() - listItems.size(), getItemCount());
            isLoadingNextMonth = false;

            if (shouldLoadPreviousMonth) {
                shouldLoadPreviousMonth = false;
                appendPreviousMonth();
            }
        }
    }
}