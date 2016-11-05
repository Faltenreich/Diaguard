package com.faltenreich.diaguard.ui.fragment;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.faltenreich.diaguard.data.dao.FoodDao;
import com.faltenreich.diaguard.data.entity.Food;

/**
 * Created by Faltenreich on 27.09.2016.
 */

public abstract class BaseFoodFragment extends BaseFragment {

    public static final String EXTRA_FOOD_ID = "EXTRA_FOOD_ID";

    private @DrawableRes int icon;
    private Food food;

    public BaseFoodFragment(@LayoutRes int layoutResId, @StringRes int titleResId, @DrawableRes int icon) {
        super(layoutResId, titleResId);
        this.icon = icon;
    }

    public BaseFoodFragment(@LayoutRes int layoutResId, @StringRes int titleResId) {
        super(layoutResId, titleResId);
        this.icon = -1;
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
            food = FoodDao.getInstance().get(food.getId());
        }
    }

    private void checkIntents() {
        if (getActivity().getIntent() != null && getActivity().getIntent().getExtras() != null) {
            Bundle extras = getActivity().getIntent().getExtras();
            if (extras.getLong(EXTRA_FOOD_ID) >= 0) {
                long foodId = extras.getLong(EXTRA_FOOD_ID);
                this.food = FoodDao.getInstance().get(foodId);
            }
        }
    }

    protected Food getFood() {
        return food;
    }

    public @DrawableRes int getIcon() {
        return icon;
    }
}
