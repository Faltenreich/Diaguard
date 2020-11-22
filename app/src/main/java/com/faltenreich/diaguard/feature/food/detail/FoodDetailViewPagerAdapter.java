package com.faltenreich.diaguard.feature.food.detail;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.faltenreich.diaguard.feature.navigation.TabDescribing;
import com.faltenreich.diaguard.shared.data.database.entity.Food;

class FoodDetailViewPagerAdapter extends FragmentStatePagerAdapter {

    private final Context context;
    private final Food food;

    FoodDetailViewPagerAdapter(FragmentManager fragmentManager, Food food) {
        super(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.context = fragmentManager.getFragments().get(0).getContext();
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
        if (fragment instanceof TabDescribing) {
            TabDescribing describing = (TabDescribing) fragment;
            return context.getString(describing.getTabProperties().getTitleResId());
        }
        return null;
    }
}
