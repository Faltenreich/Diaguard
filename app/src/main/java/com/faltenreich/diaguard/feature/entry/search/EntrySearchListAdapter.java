package com.faltenreich.diaguard.feature.entry.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.shared.data.database.entity.Tag;
import com.faltenreich.diaguard.shared.view.recyclerview.adapter.EndlessAdapter;
import com.faltenreich.diaguard.feature.log.entry.LogEntryListItem;

public class EntrySearchListAdapter extends EndlessAdapter<LogEntryListItem, EntrySearchViewHolder> {

    private OnSearchItemClickListener listener;

    EntrySearchListAdapter(Context context, OnSearchItemClickListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    @NonNull
    public EntrySearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EntrySearchViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.list_item_log_entry, parent, false), listener);
    }

    @Override
    public void onBindViewHolder(@NonNull EntrySearchViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.bindData(getItem(position));
    }

    public interface OnSearchItemClickListener {
        void onTagClicked(Tag tag, View view);
    }
}