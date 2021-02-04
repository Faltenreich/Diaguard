package com.faltenreich.diaguard.feature.preference.factor;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.faltenreich.diaguard.shared.view.recyclerview.adapter.BaseAdapter;

class FactorListAdapter extends BaseAdapter<FactorRangeItem, FactorViewHolder> {

    private final FactorViewHolder.Callback callback;

    FactorListAdapter(Context context, FactorViewHolder.Callback callback) {
        super(context);
        this.callback = callback;
    }

    @NonNull
    @Override
    public FactorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FactorViewHolder(parent, callback);
    }

    @Override
    public void onBindViewHolder(final FactorViewHolder holder, int position) {
        holder.bind(getItem(position));
    }
}
