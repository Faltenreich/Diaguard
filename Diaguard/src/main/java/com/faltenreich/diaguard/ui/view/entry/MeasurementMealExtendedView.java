package com.faltenreich.diaguard.ui.view.entry;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.FoodEditableAdapter;
import com.faltenreich.diaguard.adapter.SimpleDividerItemDecoration;
import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.data.entity.Meal;
import com.faltenreich.diaguard.data.entity.Measurement;

import butterknife.BindView;

/**
 * Created by Faltenreich on 03.10.2016.
 */

public class MeasurementMealExtendedView extends MeasurementAbstractView<Meal> {

    @BindView(R.id.list_item_measurement_meal_extended_food_item_list) RecyclerView foodList;

    private FoodEditableAdapter adapter;

    public MeasurementMealExtendedView(Context context) {
        super(context, Measurement.Category.MEAL);
    }

    public MeasurementMealExtendedView(Context context, Meal measurement) {
        super(context, measurement);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.list_item_measurement_meal_extended;
    }

    @Override
    protected void initLayout() {
        this.adapter = new FoodEditableAdapter(getContext());
        this.foodList.setLayoutManager(new LinearLayoutManager(getContext()));
        this.foodList.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        this.foodList.setAdapter(this.adapter);

        findViewById(R.id.list_item_measurement_meal_extended_food_item_add).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                addFood();
            }
        });

        addTestFood();
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

    private void addFood() {
        //getContext().startActivity(new Intent(getContext(), FoodSearchActivity.class));
        addTestFood();
    }

    private void addTestFood() {
        Food food = new Food();
        food.setName("Keks");
        food.setCarbohydrates(24);
        food.setImageUrl("https://upload.wikimedia.org/wikipedia/commons/a/a3/Mischbrot-1.jpg");
        adapter.addItem(food);
        adapter.notifyDataSetChanged();
    }
}
