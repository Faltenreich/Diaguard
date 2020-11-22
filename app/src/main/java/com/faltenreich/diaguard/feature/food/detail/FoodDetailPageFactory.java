package com.faltenreich.diaguard.feature.food.detail;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.faltenreich.diaguard.feature.food.BaseFoodFragment;
import com.faltenreich.diaguard.feature.food.detail.history.FoodHistoryFragment;
import com.faltenreich.diaguard.feature.food.detail.info.FoodInfoFragment;
import com.faltenreich.diaguard.feature.food.detail.nutrient.NutrientListFragment;
import com.faltenreich.diaguard.shared.data.database.entity.Food;

class FoodDetailPageFactory {

    static Fragment createFragmentForPage(FoodDetailPage page, Food food) {
        Fragment fragment = createFragmentForPage(page);
        fragment.setArguments(createArgumentsForFood(food));
        return fragment;
    }

    private static Fragment createFragmentForPage(FoodDetailPage page) {
        switch (page) {
            case INFO:
                return new FoodInfoFragment();
            case NUTRIENTS:
                return new NutrientListFragment();
            case HISTORY:
                return new FoodHistoryFragment();
            default:
                throw new IllegalArgumentException("Unknown fragment for page " + page);
        }
    }

    private static Bundle createArgumentsForFood(Food food) {
        Bundle bundle = new Bundle();
        bundle.putLong(BaseFoodFragment.EXTRA_FOOD_ID, food.getId());
        return bundle;
    }
}
