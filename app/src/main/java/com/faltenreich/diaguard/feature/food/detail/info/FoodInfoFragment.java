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
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.data.database.entity.FoodType;
import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;
import com.faltenreich.diaguard.shared.data.primitive.StringUtils;
import com.faltenreich.diaguard.shared.data.repository.FoodRepository;
import com.faltenreich.diaguard.shared.event.data.FoodDeletedEvent;
import com.faltenreich.diaguard.shared.view.fragment.BaseFragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        food = FoodRepository.getInstance().getById(foodId);
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

        List<String> labels = new ArrayList<>();
        if (!StringUtils.isBlank(food.getLabels())) {
            labels.addAll(Arrays.asList(food.getLabels().split(",")));
        }
        // Common food has been labelled during import but branded or custom food has not
        if (food.isBrandedFood()) {
            labels.add(getString(FoodType.BRANDED.getLabelResource()));
        } else if (food.isCustomFood(requireContext())) {
            labels.add(getString(FoodType.CUSTOM.getLabelResource()));
        }

        if (labels.isEmpty()) {
            labelsLayout.setVisibility(View.GONE);
        } else {
            labelsLayout.setVisibility(View.VISIBLE);
            for (String label : labels) {
                labelsLayout.addView(new FoodInfoLabelView(getContext(), label));
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FoodDeletedEvent event) {
        if (food.equals(event.context)) {
            finish();
        }
    }
}
