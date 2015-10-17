package com.faltenreich.diaguard.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.entity.Insulin;
import com.faltenreich.diaguard.ui.activity.EntryDetailActivity;
import com.faltenreich.diaguard.ui.activity.NewEventActivity;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.BloodSugar;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.ui.fragments.EntryDetailFragment;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.ui.viewholder.BaseViewHolder;
import com.faltenreich.diaguard.ui.viewholder.LogDayViewHolder;
import com.faltenreich.diaguard.ui.viewholder.LogMonthViewHolder;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Filip on 04.11.13.
 */
public class LogRecyclerAdapter extends BaseAdapter<LogListItem, BaseViewHolder<LogListItem>> {

    private enum ViewType {
        SECTION,
        ENTRY
    }

    private DateTime maxVisibleDate;
    private DateTime minVisibleDate;

    public LogRecyclerAdapter(Context context, DateTime firstVisibleDay) {
        super(context);

        minVisibleDate = firstVisibleDay.withDayOfMonth(1);
        maxVisibleDate = minVisibleDate;

        appendNextMonth();

        // Workaround to endless scrolling when after visible threshold
        if (firstVisibleDay.dayOfMonth().get() >= (firstVisibleDay.dayOfMonth().getMaximumValue() -
                EndlessScrollListener.VISIBLE_THRESHOLD) - 1) {
            appendNextMonth();
        }
    }

    public void appendRows(EndlessScrollListener.Direction direction) {
        if (direction == EndlessScrollListener.Direction.DOWN) {
            appendNextMonth();
        } else {
            appendPreviousMonth();
        }
    }

    private void appendNextMonth() {
        // Header
        addItem(new LogListSection(maxVisibleDate));
        notifyItemInserted(getItemCount() - 1);

        DateTime targetDate = maxVisibleDate.plusMonths(1);
        while (maxVisibleDate.isBefore(targetDate)) {
            addItem(new LogListEntry(maxVisibleDate, fetchData(maxVisibleDate)));
            notifyItemInserted(getItemCount() - 1);
            maxVisibleDate = maxVisibleDate.plusDays(1);
        }
    }

    private void appendPreviousMonth() {
        DateTime targetDate = minVisibleDate.minusMonths(1);
        while (minVisibleDate.isAfter(targetDate)) {
            minVisibleDate = minVisibleDate.minusDays(1);
            addItem(0, new LogListEntry(minVisibleDate, fetchData(minVisibleDate)));
            notifyItemInserted(0);
        }
        // Header
        addItem(0, new LogListSection(minVisibleDate));
        notifyItemInserted(0);
    }

    private List<Entry> fetchData(DateTime day) {
        try {
            List<Entry> entriesOfDay = EntryDao.getInstance().getEntriesOfDay(day);
            // TODO: Get rid of measurementCache (Currently required to enable lazy loading for generic measurements
            for (Entry entry : entriesOfDay) {
                List<Measurement> measurements = EntryDao.getInstance().getMeasurements(entry);
                entry.setMeasurementCache(measurements);
            }
            return entriesOfDay;
        } catch (SQLException exception) {
            Log.e("LogRecyclerAdapter", exception.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (getItemCount() == 0) {
            return ViewType.ENTRY.ordinal();
        } else {
            LogListItem item = getItem(position);
            if (item instanceof LogListSection) {
                return ViewType.SECTION.ordinal();
            } else if (item instanceof LogListEntry) {
                return ViewType.ENTRY.ordinal();
            }
        }
        return super.getItemViewType(position);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewTypeInt) {
        ViewType viewType = ViewType.values()[viewTypeInt];
        switch (viewType) {
            case SECTION:
                return new LogMonthViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.list_item_log_row_section, parent, false));
            default:
                return new LogDayViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.list_item_log_row_entry, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        LogListItem listItem = getItem(position);
        holder.bindData(listItem);
    }

    @Override
    public void onViewRecycled (BaseViewHolder holder) {
        if (holder instanceof LogDayViewHolder) {
            ((LogDayViewHolder) holder).entries.removeAllViews();
        }
    }
}