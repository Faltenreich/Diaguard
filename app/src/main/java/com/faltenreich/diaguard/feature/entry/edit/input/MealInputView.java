package com.faltenreich.diaguard.feature.entry.edit.input;

import android.content.Context;
import android.view.LayoutInflater;

import com.faltenreich.diaguard.databinding.ListItemMeasurementMealBinding;
import com.faltenreich.diaguard.feature.food.input.FoodInputView;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.data.database.entity.Meal;
import com.faltenreich.diaguard.shared.data.database.entity.Measurement;

/**
 * Created by Faltenreich on 20.09.2015.
 */
public class MealInputView extends MeasurementInputView<ListItemMeasurementMealBinding, Meal> {

    private FoodInputView foodInputView;
    private Food food;

    public MealInputView(Context context) {
        super(context, Category.MEAL);
    }

    public MealInputView(Context context, Meal meal) {
        super(context, meal);
    }

    public MealInputView(Context context, Food food) {
        this(context);
        this.food = food;
    }

    @Override
    protected ListItemMeasurementMealBinding createBinding(LayoutInflater inflater) {
        return ListItemMeasurementMealBinding.inflate(inflater, this, true);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        foodInputView = getBinding().foodInputView;
        foodInputView.addItem(food);
        foodInputView.setupWithMeal(measurement);
    }

    @Override
    public Measurement getMeasurement() {
        return foodInputView.getMeal();
    }
}