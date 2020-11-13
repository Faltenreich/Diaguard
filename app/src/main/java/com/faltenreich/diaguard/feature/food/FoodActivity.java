package com.faltenreich.diaguard.feature.food;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.ActivityFoodBinding;
import com.faltenreich.diaguard.feature.food.search.FoodSearchFragment;
import com.faltenreich.diaguard.shared.view.ViewUtils;
import com.faltenreich.diaguard.shared.view.activity.BaseActivity;

public class FoodActivity extends BaseActivity<ActivityFoodBinding> {

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
        showFragment(new FoodSearchFragment());
    }

    public void showFragment(Fragment fragment) {
        Fragment activeFragment = getSupportFragmentManager().findFragmentById(R.id.container);
        boolean isActive = activeFragment != null && activeFragment.getClass() == fragment.getClass();
        if (!isActive) {
            ViewUtils.hideKeyboard(this);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            String tag = fragment.getClass().getSimpleName();
            transaction.replace(R.id.container, fragment, tag);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.commit();
        }
    }
}
