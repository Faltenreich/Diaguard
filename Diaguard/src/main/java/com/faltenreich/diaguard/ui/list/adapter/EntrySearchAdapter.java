package com.faltenreich.diaguard.ui.list.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.ui.list.item.ListItemEntry;
import com.faltenreich.diaguard.data.entity.Tag;
import com.faltenreich.diaguard.ui.list.viewholder.EntrySearchViewHolder;

public class EntrySearchAdapter extends EndlessAdapter<ListItemEntry, EntrySearchViewHolder> {

    private OnSearchItemClickListener listener;

    public EntrySearchAdapter(Context context, OnSearchItemClickListener listener) {
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