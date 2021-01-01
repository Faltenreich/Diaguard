package com.faltenreich.diaguard.feature.food.detail.history;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.faltenreich.diaguard.shared.data.database.entity.FoodEaten;
import com.faltenreich.diaguard.shared.view.listener.OnItemSelectedListener;
import com.faltenreich.diaguard.shared.view.recyclerview.adapter.BaseAdapter;

class FoodHistoryListAdapter extends BaseAdapter<FoodEaten, FoodHistoryViewHolder> {

    private final OnItemSelectedListener<FoodEaten> listener;

    FoodHistoryListAdapter(Context context, OnItemSelectedListener<FoodEaten> listener) {
        super(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public FoodHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FoodHistoryViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(FoodHistoryViewHolder holder, int position) {
        FoodEaten foodEaten = getItem(holder.getAdapterPosition());
        holder.bind(foodEaten);
        holder.itemView.setOnClickListener((view) -> listener.onItemSelected(foodEaten));
    }
}
