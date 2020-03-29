package com.faltenreich.diaguard.feature.food.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.shared.view.recyclerview.adapter.BaseAdapter;

/**
 * Created by Faltenreich on 11.09.2016.
 */
class FoodSearchListAdapter extends BaseAdapter<FoodSearchListItem, FoodSearchViewHolder> {

    FoodSearchListAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public FoodSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FoodSearchViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.list_item_food_search, parent, false));
    }

    @Override
    public void onBindViewHolder(FoodSearchViewHolder holder, int position) {
        holder.bindData(getItem(holder.getAdapterPosition()));
    }
}
