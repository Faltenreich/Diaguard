package com.faltenreich.diaguard.feature.food.detail.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.FragmentFoodHistoryBinding;
import com.faltenreich.diaguard.feature.navigation.TabDescribing;
import com.faltenreich.diaguard.feature.navigation.TabProperties;
import com.faltenreich.diaguard.shared.data.database.dao.FoodDao;
import com.faltenreich.diaguard.shared.data.database.dao.FoodEatenDao;
import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.data.database.entity.FoodEaten;
import com.faltenreich.diaguard.shared.view.fragment.BaseFragment;
import com.faltenreich.diaguard.shared.view.recyclerview.decoration.VerticalDividerItemDecoration;

import java.util.List;

import butterknife.BindView;

public class FoodHistoryFragment extends BaseFragment<FragmentFoodHistoryBinding> implements TabDescribing {

    private static final String EXTRA_FOOD_ID = "EXTRA_FOOD_ID";

    public static FoodHistoryFragment newInstance(Long foodId) {
        FoodHistoryFragment fragment = new FoodHistoryFragment();
        Bundle arguments = new Bundle();
        arguments.putLong(EXTRA_FOOD_ID, foodId);
        fragment.setArguments(arguments);
        return fragment;
    }

    @BindView(R.id.list) RecyclerView historyList;
    @BindView(R.id.list_placeholder) TextView placeholder;

    private FoodHistoryListAdapter historyAdapter;

    private Long foodId;
    private Food food;

    public FoodHistoryFragment() {
        super(R.layout.fragment_food_history);
    }

    @Override
    protected FragmentFoodHistoryBinding createBinding(LayoutInflater layoutInflater) {
        return FragmentFoodHistoryBinding.inflate(layoutInflater);
    }

    @Override
    public TabProperties getTabProperties() {
        return new TabProperties.Builder(R.string.entries).build();
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
        historyList.setLayoutManager(new LinearLayoutManager(getContext()));
        historyList.addItemDecoration(new VerticalDividerItemDecoration(getContext()));
        historyAdapter = new FoodHistoryListAdapter(getContext());
        historyList.setAdapter(historyAdapter);
    }

    private void invalidateData() {
        food = FoodDao.getInstance().getById(foodId);
    }

    private void invalidateLayout() {
        historyAdapter.clear();
        List<FoodEaten> foodEatenList = FoodEatenDao.getInstance().getAll(food);
        historyAdapter.addItems(foodEatenList);
        historyAdapter.notifyDataSetChanged();
        placeholder.setVisibility(foodEatenList.size() == 0 ? View.VISIBLE : View.GONE);
    }
}
