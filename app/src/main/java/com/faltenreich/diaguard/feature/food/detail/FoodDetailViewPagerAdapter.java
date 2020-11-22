package com.faltenreich.diaguard.feature.food.detail;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.faltenreich.diaguard.feature.food.BaseFoodFragment;
import com.faltenreich.diaguard.feature.food.detail.history.FoodHistoryFragment;
import com.faltenreich.diaguard.feature.food.detail.info.FoodInfoFragment;
import com.faltenreich.diaguard.feature.food.detail.nutrient.NutrientListFragment;
import com.faltenreich.diaguard.feature.navigation.ToolbarBehavior;
import com.faltenreich.diaguard.shared.data.database.entity.Food;

class FoodDetailViewPagerAdapter extends FragmentStatePagerAdapter {

    private final Food food;

    private enum FoodDetailPage {
        INFO,
        NUTRIENTS,
        HISTORY
    }

    FoodDetailViewPagerAdapter(FragmentManager fragmentManager, Food food) {
        super(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.food = food;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment;

        FoodDetailPage page = FoodDetailPage.values()[position];
        switch (page) {
            case INFO:
                fragment = new FoodInfoFragment();
                break;
            case NUTRIENTS:
                fragment = new NutrientListFragment();
                break;
            case HISTORY:
                fragment = new FoodHistoryFragment();
                break;
            default:
                throw new IllegalArgumentException("Unknown page for position " + position);
        }

        Bundle bundle = new Bundle();
        bundle.putLong(BaseFoodFragment.EXTRA_FOOD_ID, food.getId());
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public int getCount() {
        return FoodDetailPage.values().length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Fragment fragment = getItem(position);
        return fragment instanceof ToolbarBehavior ? ((ToolbarBehavior) fragment).getTitle() : fragment.toString();
    }
}
