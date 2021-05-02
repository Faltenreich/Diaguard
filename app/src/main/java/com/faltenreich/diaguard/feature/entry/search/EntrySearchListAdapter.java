package com.faltenreich.diaguard.feature.entry.search;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.faltenreich.diaguard.feature.log.entry.LogEntryListItem;
import com.faltenreich.diaguard.feature.log.entry.LogEntryViewHolder;
import com.faltenreich.diaguard.shared.view.recyclerview.adapter.EndlessAdapter;

public class EntrySearchListAdapter extends EndlessAdapter<LogEntryListItem, EntrySearchViewHolder> {

    private final LogEntryViewHolder.Listener listener;

    EntrySearchListAdapter(Context context, LogEntryViewHolder.Listener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    @NonNull
    public EntrySearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EntrySearchViewHolder(parent, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull EntrySearchViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.bind(getItem(position));
    }
}