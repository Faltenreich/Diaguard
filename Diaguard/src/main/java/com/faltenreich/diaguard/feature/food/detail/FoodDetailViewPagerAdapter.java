package com.faltenreich.diaguard.feature.food.detail;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.feature.food.BaseFoodFragment;
import com.faltenreich.diaguard.feature.food.detail.info.FoodInfoFragment;
import com.faltenreich.diaguard.feature.food.detail.history.FoodHistoryFragment;
import com.faltenreich.diaguard.feature.food.detail.nutrient.NutrientListFragment;
import com.faltenreich.diaguard.feature.navigation.ToolbarBehavior;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Faltenreich on 27.10.2016.
 */

class FoodDetailViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<BaseFoodFragment> fragments;

    FoodDetailViewPagerAdapter(FragmentManager fragmentManager, Food food) {
        super(fragmentManager);
        initWithFood(food);
    }

    private void initWithFood(Food food) {
        Bundle bundle = new Bundle();
        bundle.putLong(BaseFoodFragment.EXTRA_FOOD_ID, food.getId());

        fragments = new ArrayList<>();

        FoodInfoFragment detailFragment = new FoodInfoFragment();
        detailFragment.setArguments(bundle);
        fragments.add(detailFragment);

        NutrientListFragment nutrientListFragment = new NutrientListFragment();
        nutrientListFragment.setArguments(bundle);
        fragments.add(nutrientListFragment);

        FoodHistoryFragment foodHistoryFragment = new FoodHistoryFragment();
        foodHistoryFragment.setArguments(bundle);
        fragments.add(foodHistoryFragment);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return position < fragments.size() ? fragments.get(position) : null;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Fragment fragment = getItem(position);
        return fragment instanceof ToolbarBehavior ? ((ToolbarBehavior) fragment).getTitle() : fragment.toString();
    }
}
