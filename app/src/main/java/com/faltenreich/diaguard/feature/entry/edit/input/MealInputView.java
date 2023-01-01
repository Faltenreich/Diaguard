package com.faltenreich.diaguard.feature.entry.edit.input;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;

import com.faltenreich.diaguard.databinding.ListItemMeasurementMealBinding;
import com.faltenreich.diaguard.feature.food.input.FoodInputView;
import com.faltenreich.diaguard.shared.data.database.entity.Meal;

/**
 * Created by Faltenreich on 20.09.2015.
 */
@SuppressLint("ViewConstructor")
public class MealInputView extends MeasurementInputView<ListItemMeasurementMealBinding, Meal> {

    private final FoodInputView foodInputView;

    public MealInputView(Context context, Meal meal) {
        super(context, Meal.class, meal);
        foodInputView = getBinding().foodInputView;
        foodInputView.getInputField().getEditText().setSaveEnabled(false);
    }

    @Override
    protected ListItemMeasurementMealBinding createBinding(LayoutInflater inflater) {
        return ListItemMeasurementMealBinding.inflate(inflater, this, true);
    }

    @Override
    protected void onBind(Meal measurement) {
        foodInputView.setMeal(measurement);
    }

    @Override
    public Meal getMeasurement() {
        return foodInputView.getMeal();
    }

    @Override
    public boolean hasInput() {
        return foodInputView.hasInput();
    }

    @Override
    public boolean isValid() {
        return foodInputView.isValid();
    }
}