package com.faltenreich.diaguard.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.FoodPagerAdapter;
import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.ui.activity.EntryActivity;
import com.faltenreich.diaguard.ui.activity.FoodEditActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;

/**
 * Created by Faltenreich on 27.09.2016.
 */

public class FoodFragment extends BaseFoodFragment {

    @BindView(R.id.food_image) ImageView image;
    @BindView(R.id.appbar) AppBarLayout appBarLayout;
    @BindView(R.id.scrim_top) View scrimTop;
    @BindView(R.id.scrim_bottom) View scrimBottom;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbarLayout;
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
    public void onStart() {
        super.onStart();
        setToolbarBackgroundColor(android.R.color.transparent);
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                deleteFood();
                return true;
            case R.id.action_edit:
                editFood();
                return true;
            case R.id.action_eat:
                eatFood(item.getActionView());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void init() {
        Food food = getFood();
        if (food != null) {
            boolean hasImage = !TextUtils.isEmpty(food.getFullImageUrl());
            scrimTop.setVisibility(View.GONE);
            scrimBottom.setVisibility(View.GONE);
            if (hasImage) {
                Picasso.with(getContext()).load(food.getFullImageUrl()).fit().centerCrop().into(image, new Callback() {
                    @Override
                    public void onSuccess() {
                        scrimTop.setVisibility(View.VISIBLE);
                        scrimBottom.setVisibility(View.VISIBLE);
                    }
                    @Override
                    public void onError() {
                    }
                });
            } else {
                image.setImageResource(0);
            }
            collapsingToolbarLayout.setTitleEnabled(false);

            FoodPagerAdapter adapter = new FoodPagerAdapter(getFragmentManager(), food);
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);

            /*
            // Set tab icons
            for (int position = 0; position < adapter.getCount(); position++) {
                TabLayout.Tab tab = tabLayout.getTabAt(position);
                if (tab != null) {
                    Fragment fragment = adapter.getItem(position);
                    if (fragment != null && fragment instanceof BaseFoodFragment) {
                        BaseFoodFragment foodFragment = (BaseFoodFragment) fragment;
                        tab.setIcon(foodFragment.getIcon());
                        tab.setContentDescription(foodFragment.getTitle());
                    }
                }
            }
            */
        }
    }

    private void update() {
        Food food = getFood();
        setTitle(food != null ? food.getName() : null);
    }

    private void eatFood(View view) {
        EntryActivity.show(getContext(), getFood());
    }

    private void editFood() {
        Intent intent = new Intent(getActivity(), FoodEditActivity.class);
        intent.putExtra(BaseFoodFragment.EXTRA_FOOD_ID, getFood().getId());
        startActivity(intent);
    }
}
