package com.faltenreich.diaguard.feature.food;

import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.MenuRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.shared.data.database.dao.FoodDao;
import com.faltenreich.diaguard.shared.data.database.dao.FoodEatenDao;
import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.event.Events;
import com.faltenreich.diaguard.shared.event.data.FoodDeletedEvent;
import com.faltenreich.diaguard.shared.view.fragment.BaseFragment;

/**
 * Created by Faltenreich on 27.09.2016.
 */

public abstract class BaseFoodFragment extends BaseFragment {

    public static final String EXTRA_FOOD_ID = "EXTRA_FOOD_ID";

    @DrawableRes private final int icon;
    private Food food;

    protected BaseFoodFragment(@LayoutRes int layoutResId, @StringRes int titleResId, @DrawableRes int icon, @MenuRes int menuResId) {
        super(layoutResId, titleResId, menuResId);
        this.icon = icon;
    }

    protected BaseFoodFragment(@LayoutRes int layoutResId, @StringRes int titleResId, @MenuRes int menuResId) {
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
        Bundle arguments = getArguments();
        if (arguments != null) {
            if (arguments.getLong(EXTRA_FOOD_ID) >= 0) {
                long foodId = arguments.getLong(EXTRA_FOOD_ID);
                this.food = FoodDao.getInstance().getById(foodId);
            }
        }
    }

    protected void deleteFoodIfConfirmed() {
        if (getContext() != null) {
            Food food = getFood();
            if (food != null) {
                long foodEaten = FoodEatenDao.getInstance().count(food);
                String message = String.format(getString(R.string.food_eaten_placeholder), foodEaten);
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.food_delete)
                        .setMessage(message)
                        .setNegativeButton(R.string.cancel, (dialog, id) -> {})
                        .setPositiveButton(R.string.delete, (dialog, id) -> deleteFood(food))
                        .create()
                        .show();
            }
        }
    }

    private void deleteFood(Food food) {
        FoodDao.getInstance().softDelete(food);
        Events.post(new FoodDeletedEvent(food));
        finish();
    }

    protected Food getFood() {
        return food;
    }

    public @DrawableRes int getIcon() {
        return icon;
    }
}
