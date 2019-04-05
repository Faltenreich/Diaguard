package com.faltenreich.diaguard.ui.list.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.entity.FoodEaten;
import com.faltenreich.diaguard.ui.list.viewholder.FoodEatenViewHolder;

/**
 * Created by Faltenreich on 11.09.2016.
 */
public class FoodEatenAdapter extends BaseAdapter<FoodEaten, FoodEatenViewHolder> {

    public FoodEatenAdapter(Context context) {
        super(context);
    }

    @Override
    public FoodEatenViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FoodEatenViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.list_item_food_eaten, parent, false));
    }

    @Override
    public void onBindViewHolder(FoodEatenViewHolder holder, int position) {
        holder.bindData(getItem(holder.getAdapterPosition()));
    }
}
