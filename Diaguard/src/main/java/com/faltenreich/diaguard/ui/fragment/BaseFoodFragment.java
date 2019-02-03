package com.faltenreich.diaguard.ui.fragment;

import android.os.Bundle;
import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.MenuRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.faltenreich.diaguard.data.dao.FoodDao;
import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.event.Events;
import com.faltenreich.diaguard.event.data.FoodDeletedEvent;

/**
 * Created by Faltenreich on 27.09.2016.
 */

public abstract class BaseFoodFragment extends BaseFragment {

    public static final String EXTRA_FOOD_ID = "EXTRA_FOOD_ID";

    private @DrawableRes int icon;
    private Food food;

    BaseFoodFragment(@LayoutRes int layoutResId, @StringRes int titleResId, @DrawableRes int icon, @MenuRes int menuResId) {
        super(layoutResId, titleResId, menuResId);
        this.icon = icon;
    }

    BaseFoodFragment(@LayoutRes int layoutResId, @StringRes int titleResId, @MenuRes int menuResId) {
        super(layoutResId, titleResId, menuResId);
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
            food = FoodDao.getInstance().getById(food.getId());
        }
    }

    private void checkIntents() {
        if (getActivity() != null && getActivity().getIntent() != null && getActivity().getIntent().getExtras() != null) {
            Bundle extras = getActivity().getIntent().getExtras();
            if (extras.getLong(EXTRA_FOOD_ID) >= 0) {
                long foodId = extras.getLong(EXTRA_FOOD_ID);
                this.food = FoodDao.getInstance().getById(foodId);
            }
        }
    }

    void deleteFood() {
        Food food = getFood();
        if (food != null) {
            FoodDao.getInstance().softDelete(food);
            Events.post(new FoodDeletedEvent(food));
            finish();
        }
    }

    protected Food getFood() {
        return food;
    }

    public @DrawableRes int getIcon() {
        return icon;
    }
}
