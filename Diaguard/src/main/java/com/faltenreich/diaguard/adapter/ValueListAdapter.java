package com.faltenreich.diaguard.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.list.ListItemHourValue;
import com.faltenreich.diaguard.ui.view.viewholder.FactorViewHolder;
import com.faltenreich.diaguard.util.NumberUtils;

/**
 * Created by Faltenreich on 04.09.2016.
 */
public class ValueListAdapter extends BaseAdapter<ListItemHourValue, FactorViewHolder> {

    public ValueListAdapter(Context context) {
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
                ListItemHourValue preference = getItem(holder.getAdapterPosition());
                try {
                    preference.setValue(NumberUtils.parseNumber(editable.toString()));
                } catch (NumberFormatException exception) {
                    preference.setValue(-1);
                }
            }
        });
    }
}
