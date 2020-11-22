package com.faltenreich.diaguard.feature.food;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.ActivityFoodBinding;
import com.faltenreich.diaguard.feature.food.search.FoodSearchFragment;
import com.faltenreich.diaguard.feature.navigation.Navigator;
import com.faltenreich.diaguard.feature.navigation.ToolbarManager;
import com.faltenreich.diaguard.feature.navigation.ToolbarProperties;
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
        initLayout();
        openFragment(new FoodSearchFragment(), Navigator.Operation.REPLACE, false);
        invalidateLayout();
    }

    @Override
    public void openFragment(@NonNull Fragment fragment, Operation operation, boolean addToBackStack) {
        openFragment(fragment, getSupportFragmentManager(), R.id.container, operation, addToBackStack);
    }

    private void initLayout() {
        ToolbarManager.applyToolbar(this, binding.toolbar.toolbar);
        getSupportFragmentManager().addOnBackStackChangedListener(this::invalidateLayout);
    }

    private void invalidateLayout() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment instanceof ToolbarProperties) {
            ToolbarProperties properties = (ToolbarProperties) fragment;
            binding.toolbar.toolbarTitle.setText(properties.getTitle());
        }
    }
}
