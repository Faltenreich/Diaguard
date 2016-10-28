package com.faltenreich.diaguard.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.entity.Food;

/**
 * Created by Faltenreich on 28.10.2016.
 */

public class FoodDetailFragment extends BaseFoodFragment {

    public FoodDetailFragment() {
        super(R.layout.fragment_food_detail, R.string.info, R.drawable.ic_category_meal);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        Food food = getFood();
        if (food != null) {

        }
    }
}
