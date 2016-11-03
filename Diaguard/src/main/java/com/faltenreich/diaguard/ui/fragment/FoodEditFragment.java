package com.faltenreich.diaguard.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.entity.Food;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Faltenreich on 01.11.2016.
 */

public class FoodEditFragment extends BaseFoodFragment {

    @BindView(R.id.food_edit_name) EditText nameInput;
    @BindView(R.id.food_edit_brand) EditText brandInput;
    @BindView(R.id.food_edit_meal) EditText mealInput;
    @BindView(R.id.food_edit_nutrients) RecyclerView nutrientList;
    @BindView(R.id.food_edit_ingredients) EditText ingredientsInput;

    public FoodEditFragment() {
        super(R.layout.fragment_food_edit, R.string.food_new);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.form_edit, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                // TODO
                return true;
            case R.id.action_done:
                // TODO
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void init() {
        Food food = getFood();
        if (food != null) {
            setTitle(R.string.food_edit);

            nameInput.setText(food.getName());
            brandInput.setText(food.getBrand());
            mealInput.setText(food.getValueForUi());
            ingredientsInput.setText(food.getIngredients());
        } else {
            setTitle(R.string.food_new);
        }
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.food_edit_add_nutrient)
    public void addNutrient() {

    }
}
