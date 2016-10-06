package com.faltenreich.diaguard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.ui.view.viewholder.FoodEditViewHolder;

/**
 * Created by Faltenreich on 11.09.2016.
 */
public class FoodEditableAdapter extends BaseAdapter<Food, FoodEditViewHolder> {

    public FoodEditableAdapter(Context context) {
        super(context);
    }

    @Override
    public FoodEditViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FoodEditViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.list_item_measurement_meal_extended_food_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final FoodEditViewHolder holder, int position) {
        holder.bindData(getItem(holder.getAdapterPosition()));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int removedPosition = holder.getAdapterPosition();
                removeItem(getItem(removedPosition));
                notifyItemRemoved(removedPosition);
            }
        });
    }
}
