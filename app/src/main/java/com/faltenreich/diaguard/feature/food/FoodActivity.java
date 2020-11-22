package com.faltenreich.diaguard.feature.food;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.ActivityFoodBinding;
import com.faltenreich.diaguard.feature.food.search.FoodSearchFragment;
import com.faltenreich.diaguard.feature.navigation.Navigating;
import com.faltenreich.diaguard.feature.navigation.Navigation;
import com.faltenreich.diaguard.feature.navigation.ToolbarManager;
import com.faltenreich.diaguard.feature.navigation.ToolbarProperties;
import com.faltenreich.diaguard.shared.view.activity.BaseActivity;

public class FoodActivity extends BaseActivity<ActivityFoodBinding> implements Navigating {

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

        Fragment initialFragment = new FoodSearchFragment();
        openFragment(initialFragment, Navigation.Operation.REPLACE, false);

        invalidateLayout(initialFragment);
    }

    @Override
    public void openFragment(@NonNull Fragment fragment, Navigation.Operation operation, boolean addToBackStack) {
        Navigation.openFragment(fragment, getSupportFragmentManager(), R.id.container, operation, addToBackStack);
    }

    private void initLayout() {
        ToolbarManager.applyToolbar(this, binding.toolbar.toolbar);

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container);
            invalidateLayout(currentFragment);
        });
    }

    private void invalidateLayout(Fragment fragment) {
        if (fragment instanceof ToolbarProperties) {
            ToolbarProperties properties = (ToolbarProperties) fragment;
            binding.toolbar.toolbar.setVisibility(properties.showToolbar() ? View.VISIBLE : View.GONE);
            binding.toolbar.toolbarTitle.setText(properties.getTitle());
        }
    }
}
