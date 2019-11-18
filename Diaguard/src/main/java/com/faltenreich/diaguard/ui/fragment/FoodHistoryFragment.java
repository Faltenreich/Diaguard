package com.faltenreich.diaguard.ui.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.ui.list.adapter.FoodEatenAdapter;
import com.faltenreich.diaguard.ui.list.decoration.LinearDividerItemDecoration;
import com.faltenreich.diaguard.data.dao.FoodEatenDao;
import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.data.entity.FoodEaten;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Faltenreich on 27.10.2016.
 */

public class FoodHistoryFragment extends BaseFoodFragment {

    @BindView(R.id.list) RecyclerView historyList;
    @BindView(R.id.list_placeholder) TextView placeholder;

    private FoodEatenAdapter historyAdapter;

    public FoodHistoryFragment() {
        super(R.layout.fragment_food_history, R.string.entries, R.drawable.ic_history_old, -1);
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
            historyList.addItemDecoration(new LinearDividerItemDecoration(getContext()));
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
