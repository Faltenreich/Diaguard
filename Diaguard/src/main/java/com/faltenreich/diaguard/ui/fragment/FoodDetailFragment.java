package com.faltenreich.diaguard.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.ui.view.FoodLabelView;

import butterknife.BindView;

/**
 * Created by Faltenreich on 28.10.2016.
 */

public class FoodDetailFragment extends BaseFoodFragment {

    @BindView(R.id.food_name) TextView name;
    @BindView(R.id.food_ingredients) TextView ingredients;
    @BindView(R.id.food_labels) ViewGroup labels;

    public FoodDetailFragment() {
        super(R.layout.fragment_food_detail, R.string.info, R.drawable.ic_category_meal);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        Food food = getFood();
        if (food != null) {
            name.setText(food.getFullName());
            ingredients.setText(String.format("%s: %s",
                    getString(R.string.ingredients),
                    food.getIngredients()));

            switch (food.getSugarLevel()) {
                case MODERATE:
                    labels.addView(new FoodLabelView(getContext(), R.string.sugar_level_moderate, FoodLabelView.Type.WARNING));
                    break;
                case HIGH:
                    labels.addView(new FoodLabelView(getContext(), R.string.sugar_level_high, FoodLabelView.Type.ERROR));
                    break;
                default:
            }

            if (food.getLabels() != null && food.getLabels().length() > 0) {
                for (String label : food.getLabels().split(",")) {
                    labels.addView(new FoodLabelView(getContext(), label));
                }
            }
        }
    }
}
