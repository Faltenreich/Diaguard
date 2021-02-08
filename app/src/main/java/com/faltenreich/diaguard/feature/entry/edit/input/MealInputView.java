package com.faltenreich.diaguard.feature.entry.edit.input;

import android.content.Context;
import android.view.LayoutInflater;

import com.faltenreich.diaguard.databinding.ListItemMeasurementMealBinding;
import com.faltenreich.diaguard.feature.food.input.FoodInputView;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.data.database.entity.Meal;

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