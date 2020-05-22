package com.faltenreich.diaguard.feature.preference.factor;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;
import com.faltenreich.diaguard.shared.view.recyclerview.adapter.BaseAdapter;

class FactorListAdapter extends BaseAdapter<FactorListItem, FactorViewHolder> {

    FactorListAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public FactorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FactorViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(final FactorViewHolder holder, int position) {
        holder.bind(getItem(position));
    }
}
