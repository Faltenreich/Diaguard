package com.faltenreich.diaguard.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.database.DatabaseDataSource;
import com.faltenreich.diaguard.database.DatabaseHelper;
import com.faltenreich.diaguard.database.Entry;
import com.faltenreich.diaguard.database.Model;
import com.faltenreich.diaguard.database.measurements.Measurement;
import com.faltenreich.diaguard.helpers.Helper;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Filip on 19.08.2014.
 */
public class LogEndlessAdapter extends EndlessAdapter implements PinnedSectionListView.PinnedSectionListAdapter {

    private static final int VIEW_TYPE_SECTION = 0;
    private static final int VIEW_TYPE_ENTRY = 1;

    private DatabaseDataSource dataSource;
    private List<ListItem> itemCache;
    private DateTime currentDay;

    int currentVisibleItemCount = 0;
    int itemsToLoad = 30;
    int totalItems;

    public LogEndlessAdapter(Context context) {
        super(new LogBaseAdapter(context));

        this.dataSource = new DatabaseDataSource(context);
        this.itemCache = new ArrayList<>();

        dataSource.open();
        totalItems = dataSource.count(DatabaseHelper.ENTRY);
        dataSource.close();
    }

    @Override
    protected View getPendingView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_view, parent, false);
    }

    private List<Entry> fetchData() {
        dataSource.open();
        String limits = String.format("%d,%d", currentVisibleItemCount, itemsToLoad);
        // Group by AND order by breaks query
        List<Model> models = dataSource.get(DatabaseHelper.ENTRY, null, null, null,
                null, null, DatabaseHelper.DATE + DatabaseHelper.DESCENDING, limits);

        List <Entry> entries = new ArrayList<>();
        for(Model model : models) {
            Entry entry = (Entry)model;
            List<Model> measurementModels = dataSource.get(DatabaseHelper.BLOODSUGAR, null,
                    DatabaseHelper.ENTRY_ID + "=?", new String[]{Long.toString(entry.getId())},
                    null, null, null, null);
            for(Model measurementModel : measurementModels) {
                entry.getMeasurements().add((Measurement) measurementModel);
            }
            entries.add(entry);
        }
        dataSource.close();

        return entries;
    }

    @Override
    protected boolean cacheInBackground() throws Exception {
        itemCache = new ArrayList<>();
        // Fetch next set of messages
        List<Entry> entries = fetchData();
        for(Entry entry : entries) {
            // Add section header before the first entry
            if (currentVisibleItemCount == 0 && entry == entries.get(0)) {
                ListSection listSection = new ListSection(Helper.getDateFormat().print(entry.getDate()));
                this.itemCache.add(listSection);
                currentDay = entry.getDate();
            }
            // Add section header for a new day
            else if(!entry.getDate().withTimeAtStartOfDay().isEqual(currentDay.withTimeAtStartOfDay())) {
                ListSection listSection = new ListSection(Helper.getDateFormat().print(entry.getDate()));
                itemCache.add(listSection);
                currentDay = entry.getDate();
            }
            ListEntry listEntry = new ListEntry(entry);
            itemCache.add(listEntry);
        }
        currentVisibleItemCount += entries.size();

        // Are there more results?
        return currentVisibleItemCount < totalItems;
    }

    @Override
    protected void appendCachedData() {
        ((LogBaseAdapter)getWrappedAdapter()).items.addAll(itemCache);
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == VIEW_TYPE_SECTION;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        LogBaseAdapter messageBaseAdapter = (LogBaseAdapter) getWrappedAdapter();

        if(messageBaseAdapter == null ||
                messageBaseAdapter.items == null ||
                messageBaseAdapter.items.size() <= 0 ||
                position >= messageBaseAdapter.items.size())
            return VIEW_TYPE_ENTRY;

        ListItem listItem = messageBaseAdapter.items.get(position);

        if (listItem.isSection())
            return VIEW_TYPE_SECTION;
        else
            return VIEW_TYPE_ENTRY;
    }

    public Adapter getAdapter() {
        return getWrappedAdapter();
    }
}
