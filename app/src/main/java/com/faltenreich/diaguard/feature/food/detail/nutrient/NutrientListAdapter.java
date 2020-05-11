package com.faltenreich.diaguard.feature.food.detail.nutrient;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.faltenreich.diaguard.shared.view.recyclerview.adapter.BaseAdapter;

class NutrientListAdapter extends BaseAdapter<NutrientListItem, NutrientViewHolder> {

    NutrientListAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public NutrientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NutrientViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull NutrientViewHolder holder, int position) {
        holder.bind(getItem(holder.getAdapterPosition()));
    }
}
