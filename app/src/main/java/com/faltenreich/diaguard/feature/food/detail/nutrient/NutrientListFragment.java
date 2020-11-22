package com.faltenreich.diaguard.feature.food.detail.nutrient;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.food.BaseFoodFragment;
import com.faltenreich.diaguard.feature.navigation.TabDescribing;
import com.faltenreich.diaguard.feature.navigation.TabProperties;
import com.faltenreich.diaguard.shared.Helper;
import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;
import com.faltenreich.diaguard.shared.view.recyclerview.decoration.VerticalDividerItemDecoration;

import butterknife.BindView;

/**
 * Created by Faltenreich on 27.10.2016.
 */

public class NutrientListFragment extends BaseFoodFragment implements TabDescribing {

    @BindView(R.id.food_list_nutrients) RecyclerView nutrientList;

    private NutrientListAdapter listAdapter;

    public NutrientListFragment() {
        super(R.layout.fragment_food_nutrients);
    }

    @Override
    public TabProperties getTabProperties() {
        return new TabProperties.Builder(R.string.nutriments).build();
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
        listAdapter = new NutrientListAdapter(getContext());
    }

    private void initLayout() {
        nutrientList.setLayoutManager(new LinearLayoutManager(getContext()));
        nutrientList.addItemDecoration(new VerticalDividerItemDecoration(getContext()));
        nutrientList.setAdapter(listAdapter);
    }

    private void update() {
        if (getContext() != null) {
            Food food = getFood();

            listAdapter.clear();

            for (Food.Nutrient nutrient : Food.Nutrient.values()) {
                String label = nutrient.getLabel(getContext());
                Float number = nutrient.getValue(food);

                String value = getContext().getString(R.string.placeholder);
                if (number != null && number >= 0) {
                    value = String.format("%s %s",
                        FloatUtils.parseFloat(number),
                        getContext().getString(nutrient.getUnitResId()));
                    if (nutrient == Food.Nutrient.ENERGY) {
                        value = String.format("%s %s (%s)",
                            FloatUtils.parseFloat(Helper.parseKcalToKj(number)),
                            getContext().getString(R.string.energy_acronym_2),
                            value);
                    }
                }
                listAdapter.addItem(new NutrientListItem(label, value));
            }

            listAdapter.notifyDataSetChanged();
        }
    }
}
