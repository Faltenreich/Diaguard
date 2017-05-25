package com.faltenreich.diaguard.ui.fragment;

import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.event.Events;
import com.faltenreich.diaguard.event.data.FoodDeletedEvent;
import com.faltenreich.diaguard.ui.view.FoodLabelView;
import com.faltenreich.diaguard.ui.view.TintImageView;
import com.faltenreich.diaguard.util.Helper;
import com.faltenreich.diaguard.util.ViewHelper;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Faltenreich on 28.10.2016.
 */

public class FoodDetailFragment extends BaseFoodFragment {

    @BindView(R.id.food_brand) TextView brand;
    @BindView(R.id.food_ingredients) TextView ingredients;
    @BindView(R.id.food_value) TextView value;
    @BindView(R.id.food_labels) ViewGroup labels;
    @BindView(R.id.food_sugar_level) TintImageView sugarLevelIcon;

    public FoodDetailFragment() {
        super(R.layout.fragment_food_detail, R.string.info, R.drawable.ic_category_meal);
    }

    @Override
    public void onResume() {
        super.onResume();
        Events.register(this);
        init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Events.unregister(this);
    }

    private void init() {

        ingredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingredients.setMaxLines(Integer.MAX_VALUE);
            }
        });

        Food food = getFood();
        if (food != null) {
            String placeholder = getString(R.string.placeholder);
            brand.setText(TextUtils.isEmpty(food.getBrand()) ? placeholder : food.getBrand());
            ingredients.setText(TextUtils.isEmpty(food.getIngredients()) ? placeholder : food.getIngredients());

            float mealValue = PreferenceHelper.getInstance().formatDefaultToCustomUnit(
                    Measurement.Category.MEAL,
                    food.getCarbohydrates());
            value.setText(String.format("%s %s", Helper.parseFloat(mealValue), PreferenceHelper.getInstance().getLabelForMealPer100g(getContext())));

            boolean indicateSugarLevel = food.getSugarLevel() != Food.NutrientLevel.LOW;
            sugarLevelIcon.setVisibility(indicateSugarLevel ? View.VISIBLE : View.GONE);
            sugarLevelIcon.setTintColor(ContextCompat.getColor(getContext(), food.getSugarLevel().colorResId));

            if (food.getLabels() != null && food.getLabels().length() > 0) {
                for (String label : food.getLabels().split(",")) {
                    labels.addView(new FoodLabelView(getContext(), label));
                }
            }
        }
    }

    @OnClick(R.id.food_sugar_level)
    protected void showSugarLevelInfo() {
        Food.NutrientLevel sugarLevel = getFood() != null ? getFood().getSugarLevel() : null;
        if (sugarLevel != null) {
            ViewHelper.showToast(getContext(), sugarLevel.descriptionResId);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FoodDeletedEvent event) {
        if (getFood().equals(event.context)) {
            finish();
        }
    }
}
