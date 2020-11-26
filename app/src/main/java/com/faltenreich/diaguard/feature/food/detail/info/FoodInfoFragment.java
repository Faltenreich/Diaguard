package com.faltenreich.diaguard.feature.food.detail.info;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.FragmentFoodInfoBinding;
import com.faltenreich.diaguard.feature.navigation.TabDescribing;
import com.faltenreich.diaguard.feature.navigation.TabProperties;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.data.database.dao.FoodDao;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;
import com.faltenreich.diaguard.shared.event.data.FoodDeletedEvent;
import com.faltenreich.diaguard.shared.view.fragment.BaseFragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class FoodInfoFragment extends BaseFragment<FragmentFoodInfoBinding> implements TabDescribing {

    private static final String EXTRA_FOOD_ID = "EXTRA_FOOD_ID";

    public static FoodInfoFragment newInstance(Long foodId) {
        FoodInfoFragment fragment = new FoodInfoFragment();
        Bundle arguments = new Bundle();
        arguments.putLong(EXTRA_FOOD_ID, foodId);
        fragment.setArguments(arguments);
        return fragment;
    }

    private Long foodId;
    private Food food;

    public FoodInfoFragment() {
        super(R.layout.fragment_food_info);
    }

    @Override
    protected FragmentFoodInfoBinding createBinding(LayoutInflater layoutInflater) {
        return FragmentFoodInfoBinding.inflate(layoutInflater);
    }

    @Override
    public TabProperties getTabProperties() {
        return new TabProperties.Builder(R.string.info).build();
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
        getBinding().ingredientsLabel.setOnClickListener(view -> getBinding().ingredientsLabel.setMaxLines(Integer.MAX_VALUE));
    }

    private void invalidateData() {
        food = FoodDao.getInstance().getById(foodId);
    }

    private void invalidateLayout() {
        String placeholder = getString(R.string.placeholder);
        getBinding().brandLabel.setText(TextUtils.isEmpty(food.getBrand()) ? placeholder : food.getBrand());
        getBinding().ingredientsLabel.setText(TextUtils.isEmpty(food.getIngredients()) ? placeholder : food.getIngredients());

        float mealValue = PreferenceStore.getInstance().formatDefaultToCustomUnit(
            Category.MEAL,
            food.getCarbohydrates());
        getBinding().valueLabel.setText(String.format("%s %s", FloatUtils.parseFloat(mealValue), PreferenceStore.getInstance().getLabelForMealPer100g(getContext())));

        ViewGroup labelsLayout = getBinding().labelsLayout;
        labelsLayout.removeAllViews();
        if (food.getLabels() != null && food.getLabels().length() > 0) {
            labelsLayout.setVisibility(View.VISIBLE);
            for (String label : food.getLabels().split(",")) {
                labelsLayout.addView(new FoodInfoLabelView(getContext(), label));
            }
        } else {
            labelsLayout.setVisibility(View.GONE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FoodDeletedEvent event) {
        if (food.equals(event.context)) {
            finish();
        }
    }
}
