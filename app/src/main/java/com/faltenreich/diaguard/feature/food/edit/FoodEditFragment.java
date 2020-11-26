package com.faltenreich.diaguard.feature.food.edit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.FragmentFoodEditBinding;
import com.faltenreich.diaguard.feature.food.FoodActions;
import com.faltenreich.diaguard.feature.navigation.ToolbarDescribing;
import com.faltenreich.diaguard.feature.navigation.ToolbarProperties;
import com.faltenreich.diaguard.shared.Helper;
import com.faltenreich.diaguard.shared.data.database.dao.FoodDao;
import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.event.Events;
import com.faltenreich.diaguard.shared.event.data.FoodSavedEvent;
import com.faltenreich.diaguard.shared.view.ViewUtils;
import com.faltenreich.diaguard.shared.view.edittext.StickyHintInput;
import com.faltenreich.diaguard.shared.view.fragment.BaseFragment;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class FoodEditFragment extends BaseFragment<FragmentFoodEditBinding> implements ToolbarDescribing {

    private static final String EXTRA_FOOD_ID = "EXTRA_FOOD_ID";

    public static FoodEditFragment newInstance(Long foodId) {
        FoodEditFragment fragment = new FoodEditFragment();
        Bundle arguments = new Bundle();
        arguments.putLong(EXTRA_FOOD_ID, foodId);
        fragment.setArguments(arguments);
        return fragment;
    }

    @BindView(R.id.food_edit_name) StickyHintInput nameInput;
    @BindView(R.id.food_edit_brand) StickyHintInput brandInput;
    @BindView(R.id.food_edit_ingredients) StickyHintInput ingredientsInput;
    @BindView(R.id.food_edit_nutrient_input_layout) NutrientInputLayout nutrientInputLayout;

    private Long foodId;
    private Food food;

    public FoodEditFragment() {
        super(R.layout.fragment_food_edit);
    }

    @Override
    protected FragmentFoodEditBinding createBinding(LayoutInflater layoutInflater) {
        return FragmentFoodEditBinding.inflate(layoutInflater);
    }

    @Override
    public ToolbarProperties getToolbarProperties() {
        return new ToolbarProperties.Builder()
            .setTitle(getContext(), food != null ? R.string.food_edit : R.string.food_new)
            .setMenu(R.menu.form_edit)
            .build();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestArguments();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        invalidateData();
        invalidateLayout();
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
        menu.findItem(R.id.action_delete).setVisible(food != null);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
        } else if (itemId == R.id.action_delete) {
            FoodActions.deleteFoodIfConfirmed(getContext(), food);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void requestArguments() {
        foodId = getArguments() != null ? getArguments().getLong(EXTRA_FOOD_ID) : null;
    }

    private void invalidateData() {
        if (foodId != null) {
            food = FoodDao.getInstance().getById(foodId);
        }
    }

    private void invalidateLayout() {
        nameInput.setText(food != null ? food.getName() : null);
        brandInput.setText(food != null ? food.getBrand() : null);
        ingredientsInput.setText(food != null ? food.getIngredients() : null);

        nutrientInputLayout.clearNutrients();
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
