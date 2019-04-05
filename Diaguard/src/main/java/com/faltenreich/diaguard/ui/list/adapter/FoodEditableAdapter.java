package com.faltenreich.diaguard.ui.list.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.entity.FoodEaten;
import com.faltenreich.diaguard.ui.list.viewholder.FoodEditViewHolder;

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
        startAnimation(holder.itemView, position);
    }

    public float getTotalCarbohydrates() {
        float totalCarbohydrates = 0;
        for (FoodEaten foodEaten : getItems()) {
            totalCarbohydrates += foodEaten.getCarbohydrates();
        }
        return totalCarbohydrates;
    }

    public boolean hasInput() {
        for (FoodEaten foodEaten : getItems()) {
            if (foodEaten.getAmountInGrams() > 0) {
                return true;
            }
        }
        return false;
    }

    private void startAnimation(View view, int position) {
        Animation animation = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);
        view.startAnimation(animation);
    }
}
