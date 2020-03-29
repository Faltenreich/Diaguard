package com.faltenreich.diaguard.feature.preference.factor;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.shared.view.recyclerview.adapter.BaseAdapter;
import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;

/**
 * Created by Faltenreich on 04.09.2016.
 */
public class FactorListAdapter extends BaseAdapter<FactorListItem, FactorViewHolder> {

    public FactorListAdapter(Context context) {
        super(context);
    }

    @Override
    public FactorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FactorViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.list_item_factor, parent, false));
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
