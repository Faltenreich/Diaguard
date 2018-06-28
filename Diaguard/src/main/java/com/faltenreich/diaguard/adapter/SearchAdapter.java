package com.faltenreich.diaguard.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.list.ListItemEntry;
import com.faltenreich.diaguard.ui.view.viewholder.EntrySearchViewHolder;

public class SearchAdapter extends BaseAdapter<ListItemEntry, EntrySearchViewHolder> {

    public SearchAdapter(Context context) {
        super(context);
    }

    @Override
    @NonNull
    public EntrySearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EntrySearchViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.list_item_log_entry, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EntrySearchViewHolder holder, int position) {
        holder.bindData(getItem(position));
    }
}
