package com.faltenreich.diaguard.ui.view.entry;

import android.content.Context;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.SeekBar;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.FoodAutoCompleteAdapter;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.data.entity.FoodEaten;
import com.faltenreich.diaguard.data.entity.Meal;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.util.NumberUtils;
import com.gregacucnik.EditableSeekBar;

import butterknife.BindView;

/**
 * Created by Faltenreich on 20.09.2015.
 */
public class MeasurementMealView extends MeasurementAbstractView<Meal> implements EditableSeekBar.OnEditableSeekBarChangeListener {

    private static final int SEEK_BAR_STEP = 100;

    @BindView(R.id.meal_value)
    protected EditText value;

    @BindView(R.id.meal_name)
    protected AutoCompleteTextView name;

    @BindView(R.id.meal_amount_in_grams)
    protected EditableSeekBar amount;

    private FoodAutoCompleteAdapter adapter;
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
        adapter = new FoodAutoCompleteAdapter(getContext());
        name.setAdapter(adapter);
        name.setThreshold(1);
        amount.setOnEditableSeekBarChangeListener(this);
    }

    @Override
    protected void setValues() {
        value.setText(measurement.getValuesForUI()[0]);
        if (measurement.getFoodEaten() != null) {
            FoodEaten foodEaten = measurement.getFoodEaten();
            name.setText(foodEaten.getFood() != null ? foodEaten.getFood().getName() : null);
            amount.setValue((int) foodEaten.getAmountInGrams());
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
                foodEaten.setAmountInGrams(amount.getValue());
                foodEaten.setMeal(measurement);
                foodEaten.setFood(food);
            }
            return measurement;
        } else {
            return null;
        }
    }

    @Override
    public void onEditableSeekBarProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            int rounded = (progress + (SEEK_BAR_STEP / 2)) / SEEK_BAR_STEP * SEEK_BAR_STEP;
            amount.setValue(rounded);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onEnteredValueTooHigh() {

    }

    @Override
    public void onEnteredValueTooLow() {

    }

    @Override
    public void onEditableSeekBarValueChanged(int value) {

    }
}