package com.faltenreich.diaguard.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.dao.FoodDao;
import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.event.Events;
import com.faltenreich.diaguard.event.data.FoodSavedEvent;
import com.faltenreich.diaguard.ui.view.StickyHintInput;
import com.faltenreich.diaguard.util.Helper;
import com.faltenreich.diaguard.util.NumberUtils;

import butterknife.BindView;

/**
 * Created by Faltenreich on 01.11.2016.
 */

public class FoodEditFragment extends BaseFoodFragment {

    @BindView(R.id.food_edit_name) StickyHintInput nameInput;
    @BindView(R.id.food_edit_brand) StickyHintInput brandInput;
    @BindView(R.id.food_edit_ingredients) StickyHintInput ingredientsInput;
    @BindView(R.id.food_edit_carbohydrates) StickyHintInput valueInput;

    public FoodEditFragment() {
        super(R.layout.fragment_food_edit, R.string.food_new, R.menu.form_edit);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.action_delete).setVisible(getFood() != null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                deleteFood();
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
            valueInput.setText(Helper.parseFloat(
                    PreferenceHelper.getInstance().formatDefaultToCustomUnit(
                            Measurement.Category.MEAL,
                            food.getCarbohydrates())));
        }
    }

    private boolean isValid() {
        boolean isValid = true;
        if (nameInput.getText().length() == 0) {
            nameInput.setError(getString(R.string.validator_value_empty));
            isValid = false;
        }
        // Check for carbohydrates
        if (valueInput.getText().length() == 0) {
            valueInput.setError(getString(R.string.validator_value_empty));
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
            food.setLanguageCode(Helper.getLanguageCode());
            food.setName(nameInput.getText());
            food.setBrand(brandInput.getText());
            food.setIngredients(ingredientsInput.getText());
            food.setCarbohydrates(NumberUtils.parseNumber(valueInput.getText()));

            FoodDao.getInstance().createOrUpdate(food);
            Events.post(new FoodSavedEvent(food));

            finish();
        }
    }
}
