package com.faltenreich.diaguard.ui.list.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.ui.list.item.ListItemExportHistory;
import com.faltenreich.diaguard.ui.list.viewholder.ExportHistoryViewHolder;

public class ExportHistoryListAdapter extends BaseAdapter<ListItemExportHistory, ExportHistoryViewHolder> {

    public ExportHistoryListAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ExportHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ExportHistoryViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.list_item_export_history, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ExportHistoryViewHolder holder, int position) {
        holder.bindData(getItem(position));
    }
}
