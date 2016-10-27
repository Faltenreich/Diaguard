package com.faltenreich.diaguard.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.NutrientAdapter;
import com.faltenreich.diaguard.adapter.SimpleDividerItemDecoration;
import com.faltenreich.diaguard.data.entity.Food;

import butterknife.BindView;

/**
 * Created by Faltenreich on 27.10.2016.
 */

public class NutrientsFragment extends BaseFoodFragment {

    @BindView(R.id.food_list_nutrients) RecyclerView nutrientList;

    public NutrientsFragment() {
        super(R.layout.fragment_nutrients, R.string.nutrients);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        Food food = getFood();
        if (food != null) {
            nutrientList.setLayoutManager(new LinearLayoutManager(getContext()));
            nutrientList.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
            nutrientList.setAdapter(new NutrientAdapter(getContext(), food));
        }
    }
}
