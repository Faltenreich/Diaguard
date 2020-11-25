package com.faltenreich.diaguard.feature.food.detail.info;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.faltenreich.diaguard.R;
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

import butterknife.BindView;

public class FoodInfoFragment extends BaseFragment implements TabDescribing {

    private static final String EXTRA_FOOD_ID = "EXTRA_FOOD_ID";

    public static FoodInfoFragment newInstance(long foodId) {
        FoodInfoFragment fragment = new FoodInfoFragment();
        Bundle arguments = new Bundle();
        arguments.putLong(EXTRA_FOOD_ID, foodId);
        fragment.setArguments(arguments);
        return fragment;
    }

    @BindView(R.id.food_brand) TextView brand;
    @BindView(R.id.food_ingredients) TextView ingredients;
    @BindView(R.id.food_value) TextView value;
    @BindView(R.id.food_labels) ViewGroup labels;

    private long foodId;
    private Food food;

    public FoodInfoFragment() {
        super(R.layout.fragment_food_info);
    }

    @Override
    public TabProperties getTabProperties() {
        return new TabProperties.Builder(R.string.info).build();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        init();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLayout();
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    private void init() {
        foodId = requireArguments().getLong(EXTRA_FOOD_ID);
    }

    private void initLayout() {
        ingredients.setOnClickListener(view -> ingredients.setMaxLines(Integer.MAX_VALUE));
    }

    private void update() {
        food = FoodDao.getInstance().getById(foodId);

        String placeholder = getString(R.string.placeholder);
        brand.setText(TextUtils.isEmpty(food.getBrand()) ? placeholder : food.getBrand());
        ingredients.setText(TextUtils.isEmpty(food.getIngredients()) ? placeholder : food.getIngredients());

        float mealValue = PreferenceStore.getInstance().formatDefaultToCustomUnit(
            Category.MEAL,
            food.getCarbohydrates());
        value.setText(String.format("%s %s", FloatUtils.parseFloat(mealValue), PreferenceStore.getInstance().getLabelForMealPer100g(getContext())));

        labels.removeAllViews();
        if (food.getLabels() != null && food.getLabels().length() > 0) {
            labels.setVisibility(View.VISIBLE);
            for (String label : food.getLabels().split(",")) {
                labels.addView(new FoodInfoLabelView(getContext(), label));
            }
        } else {
            labels.setVisibility(View.GONE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FoodDeletedEvent event) {
        if (food.equals(event.context)) {
            finish();
        }
    }
}
