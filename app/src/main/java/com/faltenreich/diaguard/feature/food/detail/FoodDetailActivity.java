package com.faltenreich.diaguard.feature.food.detail;

import android.view.LayoutInflater;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.ActivityFoodDetailBinding;
import com.faltenreich.diaguard.shared.view.activity.BaseActivity;

public class FoodDetailActivity extends BaseActivity<ActivityFoodDetailBinding> {

    public FoodDetailActivity() {
        super(R.layout.activity_food_detail);
    }

    @Override
    protected ActivityFoodDetailBinding createBinding(LayoutInflater layoutInflater) {
        return ActivityFoodDetailBinding.inflate(layoutInflater);
    }
}