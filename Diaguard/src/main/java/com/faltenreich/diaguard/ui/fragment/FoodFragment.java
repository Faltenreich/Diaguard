package com.faltenreich.diaguard.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.dao.FoodDao;
import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.ui.activity.EntryActivity;
import com.faltenreich.diaguard.ui.activity.FoodActivity;
import com.squareup.picasso.Picasso;

import butterknife.BindView;

/**
 * Created by Faltenreich on 27.09.2016.
 */

public class FoodFragment extends BaseFragment {

    public static final String EXTRA_FOOD_ID = "EXTRA_FOOD_ID";

    @BindView(R.id.food_image) ImageView image;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbarLayout;

    private Food food;

    public FoodFragment() {
        super(R.layout.fragment_food);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        checkIntents();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.food, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_entry: {
                createEntry();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
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
        }
    }

    private void createEntry() {
        Intent intent = new Intent(getActivity(), EntryActivity.class);
        intent.putExtra(EntryActivity.EXTRA_FOOD, food.getId());
        startActivity(intent);
    }
}
