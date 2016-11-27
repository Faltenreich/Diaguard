package com.faltenreich.diaguard.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.FoodEatenAdapter;
import com.faltenreich.diaguard.adapter.SimpleDividerItemDecoration;
import com.faltenreich.diaguard.data.dao.FoodEatenDao;
import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.data.entity.FoodEaten;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Faltenreich on 27.10.2016.
 */

public class FoodHistoryFragment extends BaseFoodFragment {

    @BindView(R.id.food_list_history) RecyclerView historyList;
    @BindView(R.id.food_list_placeholder) TextView placeholder;

    private FoodEatenAdapter historyAdapter;

    public FoodHistoryFragment() {
        super(R.layout.fragment_food_history, R.string.entries, R.drawable.ic_history);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
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
            historyList.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
            historyAdapter = new FoodEatenAdapter(getContext());
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
