package com.faltenreich.diaguard.ui.view.entry;

import android.content.Context;
import android.view.animation.AccelerateInterpolator;
import android.widget.EditText;
import android.widget.TextView;

import com.daasuu.cat.CountAnimationTextView;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.data.entity.Meal;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.ui.view.FoodListView;
import com.faltenreich.diaguard.util.Helper;
import com.faltenreich.diaguard.util.NumberUtils;
import com.faltenreich.diaguard.util.StringUtils;

import butterknife.BindView;

/**
 * Created by Faltenreich on 20.09.2015.
 */
public class MeasurementMealView extends MeasurementAbstractView<Meal> {

    @BindView(R.id.list_item_measurement_meal_food_item_value_input) EditText valueInput;
    @BindView(R.id.list_item_measurement_meal_food_item_value_calculated) CountAnimationTextView valueCalculated;
    @BindView(R.id.list_item_measurement_meal_food_item_value_sign) TextView valueSign;
    @BindView(R.id.list_item_measurement_meal_food_list) FoodListView foodList;

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
        valueInput.setHint(PreferenceHelper.getInstance().getUnitAcronym(Measurement.Category.MEAL));

        foodList.setMeal(measurement);
        foodList.setOnContentChangeListener(new FoodListView.OnContentChangeListener() {
            @Override
            public void onContentChanged() {
                updateUi();
            }
        });

        if (food != null) {
            foodList.addItem(food);
        }

        this.valueCalculated.setCountAnimationListener(new CountAnimationTextView.CountAnimationListener() {
            @Override
            public void onAnimationStart(Object animatedValue) {
            }
            @Override
            public void onAnimationEnd(Object animatedValue) {
                float totalCarbohydrates = foodList.getTotalCarbohydrates();
                float totalMeal = PreferenceHelper.getInstance().formatDefaultToCustomUnit(Measurement.Category.MEAL, totalCarbohydrates);
                valueCalculated.setText(Helper.parseFloat(totalMeal));
            }
        });

        updateUi();
    }

    @Override
    protected void setValues() {
        valueInput.setText(measurement.getValuesForUI()[0]);
        foodList.addItems(measurement.getFoodEaten());
    }

    @Override
    protected boolean isValid() {
        boolean isValid = true;

        String input = valueInput.getText().toString().trim();

        if (StringUtils.isBlank(input) && foodList.getTotalCarbohydrates() == 0) {
            valueInput.setError(getContext().getString(R.string.validator_value_empty));
            isValid = false;
        } else {
            if (!StringUtils.isBlank(input)) {
                isValid = isValueValid(valueInput);
            }

            // TODO: Check if every food eaten is valid
        }
        return isValid;
    }

    @Override
    public Measurement getMeasurement() {
        if (isValid()) {
            measurement.setValues(valueInput.getText().toString().length() > 0 ?
                    PreferenceHelper.getInstance().formatCustomToDefaultUnit(
                            measurement.getCategory(),
                            NumberUtils.parseNumber(valueInput.getText().toString())) : 0);
            measurement.setFoodEatenCache(foodList.getItems());
            return measurement;
        } else {
            return null;
        }
    }

    private void updateUi() {
        float oldValue = NumberUtils.parseNumber(valueCalculated.getText().toString());
        float newValue = PreferenceHelper.getInstance().formatDefaultToCustomUnit(Measurement.Category.MEAL, foodList.getTotalCarbohydrates());
        boolean hasFoodEaten = newValue > 0;
        valueCalculated.setVisibility(hasFoodEaten ? VISIBLE : GONE);
        valueSign.setVisibility(hasFoodEaten ? VISIBLE : GONE);

        boolean hasChangedSignificantly = Math.abs(newValue - oldValue) > 5;
        if (hasChangedSignificantly) {
            valueCalculated.setInterpolator(new AccelerateInterpolator()).countAnimation((int) oldValue, (int) newValue);
        } else {
            valueCalculated.setText(Helper.parseFloat(newValue));
        }
    }
}