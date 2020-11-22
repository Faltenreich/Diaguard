package com.faltenreich.diaguard.feature.food.detail.history;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.food.BaseFoodFragment;
import com.faltenreich.diaguard.feature.navigation.TabDescribing;
import com.faltenreich.diaguard.feature.navigation.TabProperties;
import com.faltenreich.diaguard.shared.data.database.dao.FoodEatenDao;
import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.data.database.entity.FoodEaten;
import com.faltenreich.diaguard.shared.view.recyclerview.decoration.VerticalDividerItemDecoration;

import java.util.List;

import butterknife.BindView;

public class FoodHistoryFragment extends BaseFoodFragment implements TabDescribing {

    @BindView(R.id.list) RecyclerView historyList;
    @BindView(R.id.list_placeholder) TextView placeholder;

    private FoodHistoryListAdapter historyAdapter;

    public FoodHistoryFragment() {
        super(R.layout.fragment_food_history);
    }

    @Override
    public TabProperties getTabProperties() {
        return new TabProperties.Builder(R.string.entries).build();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    private void init() {
        Food food = getFood();
        if (food != null) {
            historyList.setLayoutManager(new LinearLayoutManager(getContext()));
            historyList.addItemDecoration(new VerticalDividerItemDecoration(getContext()));
            historyAdapter = new FoodHistoryListAdapter(getContext());
            historyList.setAdapter(historyAdapter);
        }
    }

    private void update() {
        historyAdapter.clear();
        List<FoodEaten> foodEatenList = FoodEatenDao.getInstance().getAll(getFood());
        historyAdapter.addItems(foodEatenList);
        historyAdapter.notifyDataSetChanged();
        placeholder.setVisibility(foodEatenList.size() == 0 ? View.VISIBLE : View.GONE);
    }
}
