package com.faltenreich.diaguard.feature.food.edit;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.food.BaseFoodFragment;
import com.faltenreich.diaguard.feature.navigation.ToolbarProperties;
import com.faltenreich.diaguard.shared.Helper;
import com.faltenreich.diaguard.shared.data.database.dao.FoodDao;
import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.event.Events;
import com.faltenreich.diaguard.shared.event.data.FoodSavedEvent;
import com.faltenreich.diaguard.shared.view.ViewUtils;
import com.faltenreich.diaguard.shared.view.edittext.StickyHintInput;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Faltenreich on 01.11.2016.
 */

public class FoodEditFragment extends BaseFoodFragment {

    @BindView(R.id.food_edit_name) StickyHintInput nameInput;
    @BindView(R.id.food_edit_brand) StickyHintInput brandInput;
    @BindView(R.id.food_edit_ingredients) StickyHintInput ingredientsInput;
    @BindView(R.id.food_edit_nutrient_input_layout) NutrientInputLayout nutrientInputLayout;

    public static FoodEditFragment newInstance(Food food) {
        FoodEditFragment fragment = new FoodEditFragment();
        Bundle arguments = new Bundle();
        arguments.putLong(BaseFoodFragment.EXTRA_FOOD_ID, food.getId());
        fragment.setArguments(arguments);
        return fragment;
    }

    public FoodEditFragment() {
        super(R.layout.fragment_food_edit, R.string.food_new, R.menu.form_edit);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    @Override
    public void onPause() {
        if (getView() != null) {
            ViewUtils.hideKeyboard(getView());
        }
        super.onPause();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.action_delete).setVisible(getFood() != null);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
        } else if (itemId == R.id.action_delete) {
            deleteFoodIfConfirmed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public ToolbarProperties getToolbarProperties() {
        return new ToolbarProperties(
            getString(getFood() != null ? R.string.food_edit : R.string.food_new),
            super.getToolbarProperties().showToolbar()
        );
    }

    private void init() {
        Food food = getFood();
        if (food != null) {
            nameInput.setText(food.getName());
            brandInput.setText(food.getBrand());
            ingredientsInput.setText(food.getIngredients());
        }
        for (Food.Nutrient nutrient : Food.Nutrient.values()) {
            nutrientInputLayout.addNutrient(nutrient, food != null ? nutrient.getValue(food) : null);
        }
    }

    private boolean isValid() {
        boolean isValid = true;
        if (nameInput.getText().length() == 0) {
            nameInput.setError(getString(R.string.validator_value_empty));
            isValid = false;
        }
        return isValid;
    }

    @OnClick(R.id.fab)
    public void store() {
        if (isValid()) {
            Food food = getFood();
            if (food == null) {
                food = new Food();
            }
            food.setLanguageCode(Helper.getLanguageCode());
            food.setName(nameInput.getText());
            food.setBrand(brandInput.getText());
            food.setIngredients(ingredientsInput.getText());

            for (Map.Entry<Food.Nutrient, Float> entry : nutrientInputLayout.getValues().entrySet()) {
                Food.Nutrient nutrient = entry.getKey();
                Float value = entry.getValue();

                // Auto-fill carbohydrates for user
                if (nutrient == Food.Nutrient.CARBOHYDRATES && value == null) {
                    value = 0f;
                }

                nutrient.setValue(food, value != null ? value : -1);
            }

            FoodDao.getInstance().createOrUpdate(food);
            Events.post(new FoodSavedEvent(food));

            finish();
        }
    }
}
