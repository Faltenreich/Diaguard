package com.faltenreich.diaguard.adapters.list;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.database.DatabaseFacade;
import com.faltenreich.diaguard.database.Entry;
import com.faltenreich.diaguard.helpers.Helper;

import org.joda.time.DateTime;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Filip on 19.08.2014.
 */
public class LogEndlessAdapter extends EndlessAdapter implements PinnedSectionListView.PinnedSectionListAdapter {

    private static final int VIEW_TYPE_SECTION = 0;
    private static final int VIEW_TYPE_ENTRY = 1;

    private List<ListItem> itemCache;
    private DateTime currentDay;

    int currentVisibleItemCount = 0;
    int itemsToLoad = 30;

    public LogEndlessAdapter(Context context) {
        super(new LogBaseAdapter(context));
        this.itemCache = new ArrayList<>();
    }

    @Override
    protected View getPendingView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_view, parent, false);
    }

    private List<Entry> fetchData() {
        try {
            return DatabaseFacade.getInstance().getAll(Entry.class, currentVisibleItemCount, itemsToLoad, Entry.DATE, false);
        } catch (SQLException exception) {
            Log.e("MainFragment", exception.getMessage());
            return null;
        }
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
        return true;
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

        ListItem listItem = (ListItem) messageBaseAdapter.items.get(position);

        if (listItem.isSection())
            return VIEW_TYPE_SECTION;
        else
            return VIEW_TYPE_ENTRY;
    }

    public Adapter getAdapter() {
        return getWrappedAdapter();
    }
}
