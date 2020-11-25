package com.faltenreich.diaguard.feature.food.detail;

import androidx.fragment.app.Fragment;

import com.faltenreich.diaguard.feature.food.detail.history.FoodHistoryFragment;
import com.faltenreich.diaguard.feature.food.detail.info.FoodInfoFragment;
import com.faltenreich.diaguard.feature.food.detail.nutrient.NutrientListFragment;
import com.faltenreich.diaguard.shared.data.database.entity.Food;

class FoodDetailPageFactory {

    static Fragment createFragmentForPage(FoodDetailPage page, Food food) {
        switch (page) {
            case INFO:
                return new FoodInfoFragment();
            case NUTRIENTS:
                return new NutrientListFragment();
            case HISTORY:
                return FoodHistoryFragment.newInstance(food);
            default:
                throw new IllegalArgumentException("Unknown fragment for page " + page);
        }
    }
}
