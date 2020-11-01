package com.faltenreich.diaguard.feature.food.search;

import android.view.LayoutInflater;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.ActivityFoodSearchBinding;
import com.faltenreich.diaguard.shared.view.activity.BaseActivity;

/**
 * Created by Filip on 15.11.13.
 */

public class FoodSearchActivity extends BaseActivity<ActivityFoodSearchBinding> {

    public FoodSearchActivity() {
        super(R.layout.activity_food_search);
    }

    @Override
    protected ActivityFoodSearchBinding createBinding(LayoutInflater layoutInflater) {
        return ActivityFoodSearchBinding.inflate(layoutInflater);
    }
}