package com.faltenreich.diaguard.feature.food.input;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;

import com.faltenreich.diaguard.shared.data.database.entity.FoodEaten;
import com.faltenreich.diaguard.shared.view.recyclerview.adapter.BaseAdapter;

/**
 * Created by Faltenreich on 11.09.2016.
 */
class FoodInputListAdapter extends BaseAdapter<FoodEaten, FoodInputViewHolder> {

    FoodInputListAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public FoodInputViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FoodInputViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(final FoodInputViewHolder holder, int position) {
        holder.bind(getItem(position));
        startAnimation(holder.itemView, position);
    }

    float getTotalCarbohydrates() {
        float totalCarbohydrates = 0;
        for (FoodEaten foodEaten : getItems()) {
            totalCarbohydrates += foodEaten.getCarbohydrates();
        }
        return totalCarbohydrates;
    }

    boolean hasFood() {
        return getItemCount() > 0;
    }

    boolean hasFoodEaten() {
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
