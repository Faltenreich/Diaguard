package com.faltenreich.diaguard.ui.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.data.event.data.FoodDeletedEvent;
import com.faltenreich.diaguard.ui.view.FoodLabelView;
import com.faltenreich.diaguard.util.Helper;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

public class FoodDetailFragment extends BaseFoodFragment {

    @BindView(R.id.food_brand) TextView brand;
    @BindView(R.id.food_ingredients) TextView ingredients;
    @BindView(R.id.food_value) TextView value;
    @BindView(R.id.food_labels) ViewGroup labels;

    public FoodDetailFragment() {
        super(R.layout.fragment_food_detail, R.string.info, R.drawable.ic_category_meal, -1);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        ingredients.setOnClickListener(view -> ingredients.setMaxLines(Integer.MAX_VALUE));

        Food food = getFood();
        if (food != null) {
            String placeholder = getString(R.string.placeholder);
            brand.setText(TextUtils.isEmpty(food.getBrand()) ? placeholder : food.getBrand());
            ingredients.setText(TextUtils.isEmpty(food.getIngredients()) ? placeholder : food.getIngredients());

            float mealValue = PreferenceHelper.getInstance().formatDefaultToCustomUnit(
                    Measurement.Category.MEAL,
                    food.getCarbohydrates());
            value.setText(String.format("%s %s", Helper.parseFloat(mealValue), PreferenceHelper.getInstance().getLabelForMealPer100g(getContext())));

            labels.removeAllViews();
            if (food.getLabels() != null && food.getLabels().length() > 0) {
                labels.setVisibility(View.VISIBLE);
                for (String label : food.getLabels().split(",")) {
                    labels.addView(new FoodLabelView(getContext(), label));
                }
            } else {
                labels.setVisibility(View.GONE);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FoodDeletedEvent event) {
        if (getFood().equals(event.context)) {
            finish();
        }
    }
}
