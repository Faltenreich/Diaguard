package com.faltenreich.diaguard.ui.view.entry;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.FoodEditableAdapter;
import com.faltenreich.diaguard.adapter.SimpleDividerItemDecoration;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.data.entity.Meal;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.event.Events;
import com.faltenreich.diaguard.event.ui.FoodRemovedEvent;
import com.faltenreich.diaguard.event.ui.FoodSelectedEvent;
import com.faltenreich.diaguard.ui.activity.FoodSearchActivity;
import com.faltenreich.diaguard.util.Helper;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Faltenreich on 20.09.2015.
 */
public class MeasurementMealView extends MeasurementAbstractView<Meal> {

    @BindView(R.id.list_item_measurement_meal_food_item_value) EditText value;
    @BindView(R.id.list_item_measurement_meal_food_item_mode) Spinner mode;
    @BindView(R.id.list_item_measurement_meal_food_item_extended_layout) ViewGroup extendedLayout;
    @BindView(R.id.list_item_measurement_meal_food_item_list) RecyclerView foodList;

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
        this.value.setText(measurement != null &&measurement.getCarbohydrates() > 0 ? Helper.parseFloat(measurement.getCarbohydrates()) : null);
        this.value.setHint(PreferenceHelper.getInstance().getUnitAcronym(Measurement.Category.MEAL));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.meal_modes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.mode.setAdapter(adapter);
        this.mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateUi();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        this.adapter = new FoodEditableAdapter(getContext());
        this.foodList.setLayoutManager(new LinearLayoutManager(getContext()));
        this.foodList.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        this.foodList.setAdapter(this.adapter);

        addTestFood();
        addTestFood();
        addTestFood();
        addTestFood();

        updateUi();
    }

    @Override
    protected void setValues() {

    }

    @Override
    protected boolean isValid() {
        return false;
    }

    @Override
    public Measurement getMeasurement() {
        return null;
    }

    private void addFood(Food food) {
        this.adapter.addItem(food);
        this.adapter.notifyItemInserted(this.adapter.getItemCount() - 1);
        this.value.setText(String.format("%d", (int) this.adapter.getTotalCarbohydrates()));
        updateUi();
    }

    private void updateUi() {
        boolean isManually = this.mode.getSelectedItemPosition() == 0;

        this.value.setFocusable(isManually);
        this.value.setFocusableInTouchMode(isManually);
        this.value.setClickable(isManually);
        if (!isManually) {
            requestFocus();
        }

        this.extendedLayout.setVisibility(isManually ? GONE : VISIBLE);
    }

    private void addTestFood() {
        Food food = new Food();
        food.setName("Keks");
        food.setCarbohydrates(24);
        food.setImageUrl("https://upload.wikimedia.org/wikipedia/commons/a/a3/Mischbrot-1.jpg");
        addFood(food);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.list_item_measurement_meal_food_item_add)
    public void searchForFood() {
        getContext().startActivity(new Intent(getContext(), FoodSearchActivity.class));
    }

    @SuppressWarnings("unused")
    public void onEvent(FoodSelectedEvent event) {
        addFood(event.context);
    }

    @SuppressWarnings("unused")
    public void onEvent(FoodRemovedEvent event) {
        updateUi();
    }
}