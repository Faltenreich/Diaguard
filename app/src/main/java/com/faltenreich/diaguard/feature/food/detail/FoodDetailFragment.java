package com.faltenreich.diaguard.feature.food.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.FragmentFoodDetailBinding;
import com.faltenreich.diaguard.feature.entry.edit.EntryEditFragment;
import com.faltenreich.diaguard.feature.food.FoodActions;
import com.faltenreich.diaguard.feature.food.edit.FoodEditFragment;
import com.faltenreich.diaguard.feature.navigation.ToolbarDescribing;
import com.faltenreich.diaguard.feature.navigation.ToolbarProperties;
import com.faltenreich.diaguard.shared.data.database.dao.FoodDao;
import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.view.fragment.BaseFragment;
import com.google.android.material.tabs.TabLayout;

public class FoodDetailFragment extends BaseFragment<FragmentFoodDetailBinding> implements ToolbarDescribing {

    private static final String EXTRA_FOOD_ID = "EXTRA_FOOD_ID";

    public static FoodDetailFragment newInstance(Long foodId) {
        FoodDetailFragment fragment = new FoodDetailFragment();
        Bundle arguments = new Bundle();
        arguments.putLong(EXTRA_FOOD_ID, foodId);
        fragment.setArguments(arguments);
        return fragment;
    }

    private Long foodId;
    private Food food;

    @Override
    protected FragmentFoodDetailBinding createBinding(LayoutInflater layoutInflater) {
        return FragmentFoodDetailBinding.inflate(layoutInflater);
    }

    @Override
    public ToolbarProperties getToolbarProperties() {
        return new ToolbarProperties.Builder()
            .setTitle(food != null ? food.getName() : null)
            .setMenu(R.menu.food)
            .build();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
        } else if (itemId == R.id.action_delete) {
            FoodActions.deleteFoodIfConfirmed(requireContext(), food, (food) -> finish());
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

    private void requestArguments() {
        foodId = requireArguments().getLong(EXTRA_FOOD_ID);
    }

    private void initLayout() {
        ViewPager viewPager = getBinding().viewPager;
        viewPager.setAdapter(new FoodDetailViewPagerAdapter(getChildFragmentManager(), getContext(), foodId));
        TabLayout tabLayout = getBinding().tabLayout;
        tabLayout.setupWithViewPager(viewPager);
    }

    private void invalidateData() {
        food = FoodDao.getInstance().getById(foodId);
    }

    private void invalidateLayout() {
        setTitle(food != null ? food.getName() : null);
    }

    private void eatFood() {
        openFragment(EntryEditFragment.newInstance(food), true);
    }

    private void editFood() {
        openFragment(FoodEditFragment.newInstance(foodId), true);
    }
}
