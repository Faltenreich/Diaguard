package com.faltenreich.diaguard.feature.food.edit;

import android.view.LayoutInflater;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.ActivityFoodEditBinding;
import com.faltenreich.diaguard.shared.view.activity.BaseActivity;

/**
 * Created by Filip on 15.11.13.
 */

public class FoodEditActivity extends BaseActivity<ActivityFoodEditBinding> {

    public FoodEditActivity() {
        super(R.layout.activity_food_edit);
    }

    @Override
    protected ActivityFoodEditBinding createBinding(LayoutInflater layoutInflater) {
        return ActivityFoodEditBinding.inflate(layoutInflater);
    }
}