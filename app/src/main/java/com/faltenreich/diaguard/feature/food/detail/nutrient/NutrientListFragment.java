package com.faltenreich.diaguard.feature.food.detail.nutrient;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.FragmentFoodNutrientListBinding;
import com.faltenreich.diaguard.feature.navigation.TabDescribing;
import com.faltenreich.diaguard.feature.navigation.TabProperties;
import com.faltenreich.diaguard.shared.Helper;
import com.faltenreich.diaguard.shared.data.database.dao.FoodDao;
import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;
import com.faltenreich.diaguard.shared.view.fragment.BaseFragment;
import com.faltenreich.diaguard.shared.view.recyclerview.decoration.VerticalDividerItemDecoration;

import butterknife.BindView;

public class NutrientListFragment extends BaseFragment<FragmentFoodNutrientListBinding> implements TabDescribing {

    private static final String EXTRA_FOOD_ID = "EXTRA_FOOD_ID";

    public static NutrientListFragment newInstance(long foodId) {
        NutrientListFragment fragment = new NutrientListFragment();
        Bundle arguments = new Bundle();
        arguments.putLong(EXTRA_FOOD_ID, foodId);
        fragment.setArguments(arguments);
        return fragment;
    }

    @BindView(R.id.food_list_nutrients) RecyclerView nutrientList;

    private NutrientListAdapter listAdapter;

    private long foodId;
    private Food food;

    public NutrientListFragment() {
        super(R.layout.fragment_food_nutrient_list);
    }

    @Override
    protected FragmentFoodNutrientListBinding createBinding(LayoutInflater layoutInflater) {
        return FragmentFoodNutrientListBinding.inflate(layoutInflater);
    }

    @Override
    public TabProperties getTabProperties() {
        return new TabProperties.Builder(R.string.nutriments).build();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        requestArguments();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLayout();
    }

    @Override
    public void onResume() {
        super.onResume();
        invalidateData();
        invalidateLayout();
    }

    private void requestArguments() {
        foodId = requireArguments().getLong(EXTRA_FOOD_ID);
    }

    private void initLayout() {
        listAdapter = new NutrientListAdapter(getContext());
        nutrientList.setLayoutManager(new LinearLayoutManager(getContext()));
        nutrientList.addItemDecoration(new VerticalDividerItemDecoration(getContext()));
        nutrientList.setAdapter(listAdapter);
    }

    private void invalidateData() {
        food = FoodDao.getInstance().getById(foodId);
    }

    private void invalidateLayout() {
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
