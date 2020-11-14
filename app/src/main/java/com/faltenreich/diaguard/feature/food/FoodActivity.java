package com.faltenreich.diaguard.feature.food;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.ActivityFoodBinding;
import com.faltenreich.diaguard.feature.food.search.FoodSearchFragment;
import com.faltenreich.diaguard.feature.navigation.Navigator;
import com.faltenreich.diaguard.shared.view.activity.BaseActivity;

public class FoodActivity extends BaseActivity<ActivityFoodBinding> implements Navigator {

    public FoodActivity() {
        super(R.layout.activity_food);
    }

    @Override
    protected ActivityFoodBinding createBinding(LayoutInflater layoutInflater) {
        return ActivityFoodBinding.inflate(layoutInflater);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openFragment(new FoodSearchFragment(), Navigator.Operation.REPLACE, false);
    }

    @Override
    public void openFragment(@NonNull Fragment fragment, Operation operation, boolean addToBackStack) {
        openFragment(fragment, getSupportFragmentManager(), R.id.container, operation, addToBackStack);
    }
}
