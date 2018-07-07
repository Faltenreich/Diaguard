package com.faltenreich.diaguard.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.list.ListItemEntry;
import com.faltenreich.diaguard.data.entity.Tag;
import com.faltenreich.diaguard.ui.view.viewholder.EntrySearchViewHolder;

public class SearchAdapter extends BaseAdapter<ListItemEntry, EntrySearchViewHolder> {

    private OnSearchItemClickListener listener;

    public SearchAdapter(Context context, OnSearchItemClickListener listener) {
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
        holder.bindData(getItem(position));
    }

    public interface OnSearchItemClickListener {
        void onTagClicked(Tag tag, View view);
    }
}