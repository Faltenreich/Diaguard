package com.faltenreich.diaguard.ui.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.ui.list.adapter.NutrientAdapter;
import com.faltenreich.diaguard.ui.list.decoration.LinearDividerItemDecoration;
import com.faltenreich.diaguard.ui.list.item.ListItemNutrient;
import com.faltenreich.diaguard.util.Helper;

import butterknife.BindView;

/**
 * Created by Faltenreich on 27.10.2016.
 */

public class NutrientsFragment extends BaseFoodFragment {

    @BindView(R.id.food_list_nutrients) RecyclerView nutrientList;

    private NutrientAdapter listAdapter;

    public NutrientsFragment() {
        super(R.layout.fragment_food_nutrients, R.string.nutriments, R.drawable.ic_note, -1);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        init();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLayout();
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    private void init() {
        listAdapter = new NutrientAdapter(getContext());
    }

    private void initLayout() {
        nutrientList.setLayoutManager(new LinearLayoutManager(getContext()));
        nutrientList.addItemDecoration(new LinearDividerItemDecoration(getContext()));
        nutrientList.setAdapter(listAdapter);
    }

    private void update() {
        if (getContext() != null) {
            Food food = getFood();

            listAdapter.clear();

            for (Food.Nutrient nutrient : Food.Nutrient.values()) {
                String label = nutrient.getLabel();
                Float number = nutrient.getValue(food);

                String value = getContext().getString(R.string.placeholder);
                if (number != null && number >= 0) {
                    value = String.format("%s %s",
                        Helper.parseFloat(number),
                        getContext().getString(nutrient.getUnitResId()));
                    if (nutrient == Food.Nutrient.ENERGY) {
                        value = String.format("%s %s (%s)",
                            Helper.parseFloat(Helper.parseKcalToKj(number)),
                            getContext().getString(R.string.energy_acronym_2),
                            value);
                    }
                }
                listAdapter.addItem(new ListItemNutrient(label, value));
            }

            listAdapter.notifyDataSetChanged();
        }
    }
}
