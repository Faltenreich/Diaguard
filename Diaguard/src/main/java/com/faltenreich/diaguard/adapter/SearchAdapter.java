package com.faltenreich.diaguard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.list.ListItemEntry;
import com.faltenreich.diaguard.ui.view.viewholder.LogEntryViewHolder;

/**
 * Created by Faltenreich on 06.01.2018
 */

public class SearchAdapter extends BaseAdapter<ListItemEntry, LogEntryViewHolder> {

    public SearchAdapter(Context context) {
        super(context);
    }

    @Override
    public LogEntryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LogEntryViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.list_item_log_entry, parent, false));
    }

    @Override
    public void onBindViewHolder(LogEntryViewHolder holder, int position) {
        holder.bindData(getItem(position));
    }
}
