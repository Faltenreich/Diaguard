package com.faltenreich.diaguard.feature.food.detail.history;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.faltenreich.diaguard.shared.data.database.entity.FoodEaten;
import com.faltenreich.diaguard.shared.view.recyclerview.adapter.BaseAdapter;

/**
 * Created by Faltenreich on 11.09.2016.
 */
class FoodHistoryListAdapter extends BaseAdapter<FoodEaten, FoodHistoryViewHolder> {

    FoodHistoryListAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public FoodHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FoodHistoryViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(FoodHistoryViewHolder holder, int position) {
        holder.bindData(getItem(holder.getAdapterPosition()));
    }
}
