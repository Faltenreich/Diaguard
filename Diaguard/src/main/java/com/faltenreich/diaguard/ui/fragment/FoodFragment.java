package com.faltenreich.diaguard.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;

import android.view.MenuItem;
import android.view.View;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.ui.viewpager.FoodPagerAdapter;
import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.ui.activity.EntryActivity;
import com.faltenreich.diaguard.ui.activity.FoodEditActivity;

import butterknife.BindView;

/**
 * Created by Faltenreich on 27.09.2016.
 */

public class FoodFragment extends BaseFoodFragment {

    @BindView(R.id.food_viewpager) ViewPager viewPager;
    @BindView(R.id.food_tablayout) TabLayout tabLayout;

    public FoodFragment() {
        super(R.layout.fragment_food, R.string.food, -1, R.menu.food);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        update();
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                deleteFoodIfConfirmed();
                return true;
            case R.id.action_edit:
                editFood();
                return true;
            case R.id.action_eat:
                eatFood();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void init() {
        Food food = getFood();
        if (food != null) {
            FoodPagerAdapter adapter = new FoodPagerAdapter(getFragmentManager(), food);
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    private void update() {
        Food food = getFood();
        setTitle(food != null ? food.getName() : null);
    }

    private void eatFood() {
        EntryActivity.show(getContext(), getFood());
    }

    private void editFood() {
        Intent intent = new Intent(getActivity(), FoodEditActivity.class);
        intent.putExtra(BaseFoodFragment.EXTRA_FOOD_ID, getFood().getId());
        startActivity(intent);
    }
}
