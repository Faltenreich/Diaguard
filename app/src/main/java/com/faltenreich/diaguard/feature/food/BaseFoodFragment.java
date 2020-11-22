package com.faltenreich.diaguard.feature.food;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import com.faltenreich.diaguard.shared.data.database.dao.FoodDao;
import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.view.fragment.BaseFragment;

// TODO: Replace inheritance with lifecycle observer
public abstract class BaseFoodFragment extends BaseFragment {

    public static final String EXTRA_FOOD_ID = "EXTRA_FOOD_ID";

    private Food food;

    protected BaseFoodFragment(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    protected Food getFood() {
        return food;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkIntents();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (food != null) {
            food = FoodDao.getInstance().getById(food.getId());
        }
    }

    private void checkIntents() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            if (arguments.getLong(EXTRA_FOOD_ID) >= 0) {
                long foodId = arguments.getLong(EXTRA_FOOD_ID);
                this.food = FoodDao.getInstance().getById(foodId);
            }
        }
    }
}
