package com.faltenreich.diaguard.feature.entry.edit.input;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;

import com.faltenreich.diaguard.databinding.ListItemMeasurementMealBinding;
import com.faltenreich.diaguard.feature.food.input.FoodInputView;
import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.data.database.entity.Meal;

/**
 * Created by Faltenreich on 20.09.2015.
 */
public class MealInputView extends MeasurementInputView<ListItemMeasurementMealBinding, Meal> {

    private FoodInputView foodInputView;
    private Food food;

    public MealInputView(Context context) {
        super(context);
    }

    public MealInputView(Context context, @Nullable Meal meal, @Nullable Food food) {
        super(context, Meal.class, meal);
        this.food = food;
    }

    @Override
    protected ListItemMeasurementMealBinding createBinding(LayoutInflater inflater) {
        return ListItemMeasurementMealBinding.inflate(inflater, this, true);
    }

    @Override
    protected void onBind(Meal measurement) {
        foodInputView = getBinding().foodInputView;
        foodInputView.addItem(food);
        foodInputView.setupWithMeal(measurement);
    }

    @Override
    public boolean isValid(Meal measurement) {
        return foodInputView.isValid();
    }

    @Override
    public Meal getMeasurement() {
        return foodInputView.getMeal();
    }
}