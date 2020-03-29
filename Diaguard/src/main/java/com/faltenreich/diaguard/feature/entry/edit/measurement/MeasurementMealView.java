package com.faltenreich.diaguard.feature.entry.edit.measurement;

import android.content.Context;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.data.database.entity.Meal;
import com.faltenreich.diaguard.shared.data.database.entity.Measurement;
import com.faltenreich.diaguard.feature.food.input.FoodInputView;

import butterknife.BindView;

/**
 * Created by Faltenreich on 20.09.2015.
 */
public class MeasurementMealView extends MeasurementAbstractView<Meal> {

    @BindView(R.id.list_item_measurement_meal_food_list) FoodInputView foodInputView;

    public MeasurementMealView(Context context) {
        super(context, Category.MEAL);
    }

    public MeasurementMealView(Context context, Meal meal) {
        super(context, meal);
    }

    public MeasurementMealView(Context context, Food food) {
        super(context, food);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.list_item_measurement_meal;
    }

    @Override
    protected void initLayout() {
        foodInputView.addItem(food);
    }

    @Override
    protected void setValues() {
        foodInputView.setupWithMeal(measurement);
    }

    @Override
    protected boolean isValid() {
        return foodInputView.isValid();
    }

    @Override
    public Measurement getMeasurement() {
        return foodInputView.getMeal();
    }
}