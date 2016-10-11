package com.faltenreich.diaguard.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.FoodEatenAdapter;
import com.faltenreich.diaguard.adapter.NutrientAdapter;
import com.faltenreich.diaguard.adapter.SimpleDividerItemDecoration;
import com.faltenreich.diaguard.data.dao.FoodDao;
import com.faltenreich.diaguard.data.dao.FoodEatenDao;
import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.ui.activity.EntryActivity;
import com.faltenreich.diaguard.ui.activity.FoodActivity;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Faltenreich on 27.09.2016.
 */

public class FoodFragment extends BaseFragment {

    private static final int COLUMN_COUNT_NUTRIENTS = 2;

    public static final String EXTRA_FOOD_ID = "EXTRA_FOOD_ID";

    @BindView(R.id.food_image) ImageView image;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.food_list_nutrients) RecyclerView nutrientList;
    @BindView(R.id.food_layout_history) ViewGroup historyLayout;
    @BindView(R.id.food_list_history) RecyclerView historyList;

    private Food food;

    private FoodEatenAdapter historyAdapter;

    public FoodFragment() {
        super(R.layout.fragment_food);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkIntents();
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

    private void checkIntents() {
        if (getActivity() instanceof FoodActivity && getActivity().getIntent() != null && getActivity().getIntent().getExtras() != null) {
            Bundle extras = getActivity().getIntent().getExtras();
            if (extras.getLong(EXTRA_FOOD_ID) >= 0) {
                long foodId = extras.getLong(EXTRA_FOOD_ID);
                this.food = FoodDao.getInstance().get(foodId);
            }
        }
    }

    private void init() {
        if (food != null) {
            Picasso.with(getContext()).load(food.getImageUrl()).into(image);

            collapsingToolbarLayout.setTitle(food.getName());

            nutrientList.setLayoutManager(new LinearLayoutManager(getContext()));
            nutrientList.setAdapter(new NutrientAdapter(getContext(), food));

            historyList.setLayoutManager(new LinearLayoutManager(getContext()));
            historyList.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
            historyAdapter = new FoodEatenAdapter(getContext());
            historyList.setAdapter(historyAdapter);
        }
    }

    private void update() {
        historyAdapter.clear();
        historyAdapter.addItems(FoodEatenDao.getInstance().getAll(food));
        historyAdapter.notifyDataSetChanged();

        boolean showHistory = historyAdapter.getItemCount() > 0;
        historyLayout.setVisibility(showHistory ? View.VISIBLE : View.GONE);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.food_fab)
    public void createEntry() {
        Intent intent = new Intent(getActivity(), EntryActivity.class);
        intent.putExtra(EntryActivity.EXTRA_FOOD, food.getId());
        startActivity(intent);
    }
}
