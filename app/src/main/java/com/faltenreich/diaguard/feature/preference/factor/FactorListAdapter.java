package com.faltenreich.diaguard.feature.preference.factor;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;
import com.faltenreich.diaguard.shared.view.recyclerview.adapter.BaseAdapter;

/**
 * Created by Faltenreich on 04.09.2016.
 */
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
        holder.bindData(getItem(position));
        holder.value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                FactorListItem preference = getItem(holder.getAdapterPosition());
                try {
                    preference.setValue(FloatUtils.parseNumber(editable.toString()));
                } catch (NumberFormatException exception) {
                    preference.setValue(-1);
                }
            }
        });
    }
}
