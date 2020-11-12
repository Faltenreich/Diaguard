package com.faltenreich.diaguard.feature.food.detail;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.ActivityFoodBinding;
import com.faltenreich.diaguard.shared.view.activity.BaseActivity;

public class FoodDetailActivity extends BaseActivity<ActivityFoodBinding> {

    public FoodDetailActivity() {
        super(R.layout.activity_food);
    }

    @Override
    protected ActivityFoodBinding createBinding(LayoutInflater layoutInflater) {
        return ActivityFoodBinding.inflate(layoutInflater);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager()
            .beginTransaction()
            .add(R.id.fragmentContainerView, new FoodDetailFragment())
            .commit();
    }
}