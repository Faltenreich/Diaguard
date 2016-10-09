package com.faltenreich.diaguard.ui.view.entry;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.EditText;
import android.widget.TextView;

import com.daasuu.cat.CountAnimationTextView;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.FoodEditableAdapter;
import com.faltenreich.diaguard.adapter.SimpleDividerItemDecoration;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.dao.FoodEatenDao;
import com.faltenreich.diaguard.data.dao.MeasurementDao;
import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.data.entity.FoodEaten;
import com.faltenreich.diaguard.data.entity.Meal;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.event.Events;
import com.faltenreich.diaguard.event.ui.FoodEatenRemovedEvent;
import com.faltenreich.diaguard.event.ui.FoodEatenUpdatedEvent;
import com.faltenreich.diaguard.event.ui.FoodSelectedEvent;
import com.faltenreich.diaguard.ui.activity.FoodSearchActivity;
import com.faltenreich.diaguard.ui.fragment.FoodSearchFragment;
import com.faltenreich.diaguard.util.Helper;
import com.faltenreich.diaguard.util.NumberUtils;
import com.faltenreich.diaguard.util.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Faltenreich on 20.09.2015.
 */
public class MeasurementMealView extends MeasurementAbstractView<Meal> {

    @BindView(R.id.list_item_measurement_meal_food_item_value_input) EditText valueInput;
    @BindView(R.id.list_item_measurement_meal_food_item_value_calculated) CountAnimationTextView valueCalculated;
    @BindView(R.id.list_item_measurement_meal_food_item_value_sign) TextView valueSign;
    @BindView(R.id.list_item_measurement_meal_food_item_list) RecyclerView foodList;
    @BindView(R.id.list_item_measurement_meal_food_item_separator) View separator;

    private FoodEditableAdapter adapter;

    public MeasurementMealView(Context context) {
        super(context, Measurement.Category.MEAL);
    }

    public MeasurementMealView(Context context, Meal meal) {
        super(context, meal);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Events.register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Events.unregister(this);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.list_item_measurement_meal;
    }

    @Override
    protected void initLayout() {
        // TODO: Set values from given measurement
        String mealUnit = PreferenceHelper.getInstance().getUnitAcronym(Measurement.Category.MEAL);
        this.valueInput.setHint(mealUnit);

        this.adapter = new FoodEditableAdapter(getContext());
        this.foodList.setLayoutManager(new LinearLayoutManager(getContext()));
        this.foodList.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        this.foodList.setAdapter(this.adapter);

        this.valueCalculated.setCountAnimationListener(new CountAnimationTextView.CountAnimationListener() {
            @Override
            public void onAnimationStart(Object animatedValue) {
            }
            @Override
            public void onAnimationEnd(Object animatedValue) {
                float totalCarbohydrates = adapter.getTotalCarbohydrates();
                float totalMeal = PreferenceHelper.getInstance().formatDefaultToCustomUnit(Measurement.Category.MEAL, totalCarbohydrates);
                valueCalculated.setText(Helper.parseFloat(totalMeal));
            }
        });

        updateUi();
    }

    @Override
    protected void setValues() {

    }

    @Override
    protected boolean isValid() {
        boolean isValid = true;

        String input = valueInput.getText().toString().trim();

        if (StringUtils.isBlank(input) && this.adapter.getTotalCarbohydrates() == 0) {
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

            MeasurementDao.getInstance(Meal.class).createOrUpdate(measurement);

            for (FoodEaten foodEaten : this.adapter.getItems()) {
                foodEaten.setMeal(measurement);
                FoodEatenDao.getInstance().createOrUpdate(foodEaten);
            }
            return measurement;
        } else {
            return null;
        }
    }

    private void addFood(Food food) {
        FoodEaten foodEaten = new FoodEaten();
        foodEaten.setFood(food);
        foodEaten.setMeal(measurement);

        this.adapter.addItem(foodEaten);
        this.adapter.notifyItemInserted(this.adapter.getItemCount() - 1);

        updateUi();
    }

    private void removeFoodEaten(int position) {
        this.adapter.removeItem(position);
        this.adapter.notifyItemRemoved(position);
        updateUi();
    }

    private void updateFoodEaten(FoodEaten foodEaten, int position) {
        this.adapter.updateItem(position, foodEaten);
        this.adapter.notifyItemChanged(position);
        updateUi();
    }

    private void updateUi() {
        boolean hasFood = adapter.getItemCount() > 0;
        separator.setVisibility(hasFood ? VISIBLE : GONE);

        float oldValue = NumberUtils.parseNumber(valueCalculated.getText().toString());
        float newValue = PreferenceHelper.getInstance().formatDefaultToCustomUnit(Measurement.Category.MEAL, adapter.getTotalCarbohydrates());
        // TODO: Only animate on changes
        boolean hasFoodEaten = newValue > 0;
        valueCalculated.setVisibility(hasFoodEaten ? VISIBLE : GONE);
        valueSign.setVisibility(hasFoodEaten ? VISIBLE : GONE);
        valueCalculated.setInterpolator(new AccelerateInterpolator()).countAnimation((int) oldValue, (int) newValue);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.list_item_measurement_meal_food_item_add)
    public void searchForFood() {
        Intent intent = new Intent(getContext(), FoodSearchActivity.class);
        intent.putExtra(FoodSearchFragment.EXTRA_MODE, FoodSearchFragment.Mode.SELECT);
        getContext().startActivity(intent);
    }

    @SuppressWarnings("unused")
    public void onEvent(FoodSelectedEvent event) {
        addFood(event.context);
    }

    @SuppressWarnings("unused")
    public void onEvent(FoodEatenUpdatedEvent event) {
        updateFoodEaten(event.context, event.position);
    }

    @SuppressWarnings("unused")
    public void onEvent(FoodEatenRemovedEvent event) {
        removeFoodEaten(event.position);
    }
}