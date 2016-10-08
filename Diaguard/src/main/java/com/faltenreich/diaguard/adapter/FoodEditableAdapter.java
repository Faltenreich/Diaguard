package com.faltenreich.diaguard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.event.Events;
import com.faltenreich.diaguard.event.ui.FoodRemovedEvent;
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
        return new FoodEditViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.list_item_measurement_meal_food_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final FoodEditViewHolder holder, int position) {
        holder.bindData(getItem(position));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int removedPosition = holder.getLayoutPosition();
                Food food = getItem(removedPosition);
                removeItem(removedPosition);
                notifyItemRemoved(removedPosition);
                Events.post(new FoodRemovedEvent(food));
            }
        });
    }

    public float getTotalCarbohydrates() {
        float totalCarbohydrates = 0;
        for (Food food : getItems()) {
            totalCarbohydrates += food.getCarbohydrates();
        }
        return totalCarbohydrates;
    }
}
