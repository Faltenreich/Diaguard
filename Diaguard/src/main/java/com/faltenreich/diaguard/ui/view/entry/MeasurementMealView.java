package com.faltenreich.diaguard.ui.view.entry;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.data.entity.FoodEaten;
import com.faltenreich.diaguard.data.entity.Meal;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.ui.activity.FoodSearchActivity;
import com.faltenreich.diaguard.ui.fragment.FoodSearchFragment;
import com.faltenreich.diaguard.util.NumberUtils;

import butterknife.BindView;

/**
 * Created by Faltenreich on 20.09.2015.
 */
public class MeasurementMealView extends MeasurementAbstractView<Meal> {

    @BindView(R.id.meal_value)
    protected EditText value;

    @BindView(R.id.meal_name)
    protected TextView name;

    @BindView(R.id.meal_amount_in_grams)
    protected EditText amount;

    private Food food;

    public MeasurementMealView(Context context) {
        super(context, Measurement.Category.MEAL);
    }

    public MeasurementMealView(Context context, Meal meal) {
        super(context, meal);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.list_item_measurement_meal;
    }

    @Override
    protected void initLayout() {
        value.setHint(PreferenceHelper.getInstance().getUnitAcronym(measurement.getCategory()));
        name.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FoodSearchActivity.class);
                intent.putExtra(FoodSearchFragment.EXTRA_MODE, FoodSearchFragment.Mode.SELECT);
                getContext().startActivity(intent);
            }
        });
    }

    @Override
    protected void setValues() {
        value.setText(measurement.getValuesForUI()[0]);
        if (measurement.getFoodEaten() != null) {
            FoodEaten foodEaten = measurement.getFoodEaten();
            name.setText(foodEaten.getFood() != null ? foodEaten.getFood().getName() : null);
            amount.setText(Float.toString(foodEaten.getAmountInGrams()));
        }
    }

    @Override
    protected boolean isValid() {
        return false;
    }

    @Override
    public Measurement getMeasurement() {
        if (isValid()) {
            measurement.setValues(
                    PreferenceHelper.getInstance().formatCustomToDefaultUnit(
                            measurement.getCategory(),
                            NumberUtils.parseNumber(value.getText().toString())));
            if (name.getText().length() > 0) {
                FoodEaten foodEaten = new FoodEaten();
                foodEaten.setAmountInGrams(Float.parseFloat(amount.getText().toString()));
                foodEaten.setMeal(measurement);
                foodEaten.setFood(food);
            }
            return measurement;
        } else {
            return null;
        }
    }
    
}