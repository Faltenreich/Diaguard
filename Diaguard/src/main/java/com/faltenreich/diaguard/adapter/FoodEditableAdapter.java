package com.faltenreich.diaguard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.entity.FoodEaten;
import com.faltenreich.diaguard.ui.view.viewholder.FoodEditViewHolder;

/**
 * Created by Faltenreich on 11.09.2016.
 */
public class FoodEditableAdapter extends BaseAdapter<FoodEaten, FoodEditViewHolder> {

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
    }

    public float getTotalCarbohydrates() {
        float totalCarbohydrates = 0;
        for (FoodEaten foodEaten : getItems()) {
            float carbohydrates = foodEaten.getAmountInGrams() * foodEaten.getFood().getCarbohydrates() / 100;
            totalCarbohydrates += carbohydrates;
        }
        return totalCarbohydrates;
    }
}
