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
import com.faltenreich.diaguard.data.dao.FoodDao;
import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.event.Events;
import com.faltenreich.diaguard.event.data.FoodDeletedEvent;
import com.faltenreich.diaguard.event.data.FoodSavedEvent;

import butterknife.BindView;

/**
 * Created by Faltenreich on 01.11.2016.
 */

public class FoodEditFragment extends BaseFoodFragment {

    @BindView(R.id.food_edit_name) EditText nameInput;
    @BindView(R.id.food_edit_brand) EditText brandInput;
    @BindView(R.id.food_edit_ingredients) EditText ingredientsInput;
    @BindView(R.id.food_edit_nutrients) RecyclerView nutrientList;

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
        menu.findItem(R.id.action_delete).setVisible(getFood() != null && !getFood().isSynchronized());
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                delete();
                return true;
            case R.id.action_done:
                store();
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
            ingredientsInput.setText(food.getIngredients());
        }
    }

    private boolean isValid() {
        boolean isValid = true;
        if (nameInput.getText().toString().length() == 0) {
            nameInput.setError(getString(R.string.validator_value_empty));
            isValid = false;
        }
        // Check for carbohydrates
        if (false) {
            isValid = false;
        }
        return isValid;
    }

    private void store() {
        if (isValid()) {
            Food food = getFood();
            if (food == null) {
                food = new Food();
            }
            food.setName(nameInput.getText().toString());
            food.setBrand(brandInput.getText().toString());
            food.setIngredients(ingredientsInput.getText().toString());

            FoodDao.getInstance().createOrUpdate(food);
            Events.post(new FoodSavedEvent(food));

            finish();
        }
    }

    private void delete() {
        Food food = getFood();
        if (food != null) {
            FoodDao.getInstance().delete(food);
            Events.post(new FoodDeletedEvent(food));
            finish();
        }
    }
}
