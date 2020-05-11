package com.faltenreich.diaguard.feature.export.history;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.faltenreich.diaguard.shared.view.recyclerview.adapter.BaseAdapter;

class ExportHistoryListAdapter extends BaseAdapter<ExportHistoryListItem, ExportHistoryViewHolder> {

    ExportHistoryListAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ExportHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ExportHistoryViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull final ExportHistoryViewHolder holder, int position) {
        holder.bindData(getItem(position));
    }
}
