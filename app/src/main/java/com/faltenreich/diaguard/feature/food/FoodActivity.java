package com.faltenreich.diaguard.feature.food;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.fragment.app.Fragment;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.ActivityFoodBinding;
import com.faltenreich.diaguard.feature.food.search.FoodSearchFragment;
import com.faltenreich.diaguard.feature.navigation.FragmentNavigator;
import com.faltenreich.diaguard.shared.view.activity.BaseActivity;

public class FoodActivity extends BaseActivity<ActivityFoodBinding> implements FragmentNavigator {

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
        openFragment(new FoodSearchFragment(), false);
    }

    private void openFragment(Fragment fragment, boolean addToBackStack) {
        openFragment(fragment, getSupportFragmentManager(), R.id.container, Operation.REPLACE, addToBackStack);
    }
}
