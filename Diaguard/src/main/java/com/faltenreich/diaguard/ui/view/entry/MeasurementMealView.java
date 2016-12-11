package com.faltenreich.diaguard.ui.view.entry;

import android.content.Context;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.data.entity.Meal;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.ui.view.FoodInputView;

import butterknife.BindView;

/**
 * Created by Faltenreich on 20.09.2015.
 */
public class MeasurementMealView extends MeasurementAbstractView<Meal> {

    @BindView(R.id.list_item_measurement_meal_food_list)
    FoodInputView foodList;

    public MeasurementMealView(Context context) {
        super(context, Measurement.Category.MEAL);
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
        foodList.addItem(food);
    }

    @Override
    protected void setValues() {
        foodList.setupWithMeal(measurement);
    }

    @Override
    protected boolean isValid() {
        return foodList.isValid();
    }

    @Override
    public Measurement getMeasurement() {
        return foodList.getMeal();
    }
}