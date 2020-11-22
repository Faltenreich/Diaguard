package com.faltenreich.diaguard.feature.food.detail;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.entry.edit.EntryEditActivity;
import com.faltenreich.diaguard.feature.food.BaseFoodFragment;
import com.faltenreich.diaguard.feature.food.FoodActions;
import com.faltenreich.diaguard.feature.food.edit.FoodEditFragment;
import com.faltenreich.diaguard.feature.navigation.Navigation;
import com.faltenreich.diaguard.feature.navigation.ToolbarDescribing;
import com.faltenreich.diaguard.feature.navigation.ToolbarProperties;
import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;

public class FoodDetailFragment extends BaseFoodFragment implements ToolbarDescribing {

    @BindView(R.id.food_viewpager) ViewPager viewPager;
    @BindView(R.id.food_tablayout) TabLayout tabLayout;

    public static FoodDetailFragment newInstance(Food food) {
        FoodDetailFragment fragment = new FoodDetailFragment();
        Bundle arguments = new Bundle();
        arguments.putLong(BaseFoodFragment.EXTRA_FOOD_ID, food.getId());
        fragment.setArguments(arguments);
        return fragment;
    }

    public FoodDetailFragment() {
        super(R.layout.fragment_food_detail);
    }

    @Override
    public ToolbarProperties getToolbarProperties() {
        return new ToolbarProperties.Builder()
            .setTitle(getFood().getName())
            .setMenu(R.menu.food)
            .build();
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
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
        } else if (itemId == R.id.action_delete) {
            FoodActions.deleteFoodIfConfirmed(getContext(), getFood());
            finish();
            return true;
        } else if (itemId == R.id.action_edit) {
            editFood();
            return true;
        } else if (itemId == R.id.action_eat) {
            eatFood();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        Food food = getFood();
        if (food != null) {
            FoodDetailViewPagerAdapter adapter = new FoodDetailViewPagerAdapter(getChildFragmentManager(), getContext(), food);
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    private void update() {
        Food food = getFood();
        setTitle(food != null ? food.getName() : null);
    }

    private void eatFood() {
        EntryEditActivity.show(getContext(), getFood());
    }

    private void editFood() {
        openFragment(FoodEditFragment.newInstance(getFood()), Navigation.Operation.REPLACE, true);
    }
}
