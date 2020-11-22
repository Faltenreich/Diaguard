package com.faltenreich.diaguard.feature.food.detail;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.faltenreich.diaguard.feature.navigation.ToolbarDescribing;
import com.faltenreich.diaguard.shared.data.database.entity.Food;

class FoodDetailViewPagerAdapter extends FragmentStatePagerAdapter {

    private final Food food;

    FoodDetailViewPagerAdapter(FragmentManager fragmentManager, Food food) {
        super(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.food = food;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        FoodDetailPage page = FoodDetailPage.values()[position];
        return FoodDetailPageFactory.createFragmentForPage(page, food);
    }

    @Override
    public int getCount() {
        return FoodDetailPage.values().length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Fragment fragment = getItem(position);
        return fragment instanceof ToolbarDescribing
            ? ((ToolbarDescribing) fragment).getToolbarProperties().getTitle()
            : fragment.toString();
    }
}
