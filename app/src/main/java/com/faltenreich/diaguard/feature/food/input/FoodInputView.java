package com.faltenreich.diaguard.feature.food.input;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.food.search.FoodSearchActivity;
import com.faltenreich.diaguard.feature.food.search.FoodSearchFragment;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.data.database.entity.FoodEaten;
import com.faltenreich.diaguard.shared.data.database.entity.Meal;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;
import com.faltenreich.diaguard.shared.data.primitive.StringUtils;
import com.faltenreich.diaguard.shared.event.Events;
import com.faltenreich.diaguard.shared.event.ui.FoodEatenRemovedEvent;
import com.faltenreich.diaguard.shared.event.ui.FoodEatenUpdatedEvent;
import com.faltenreich.diaguard.shared.event.ui.FoodSelectedEvent;
import com.faltenreich.diaguard.shared.view.edittext.StickyHintInput;
import com.faltenreich.diaguard.shared.view.recyclerview.decoration.VerticalDividerItemDecoration;
import com.j256.ormlite.dao.ForeignCollection;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Faltenreich on 13.10.2016.
 */

public class FoodInputView extends LinearLayout {

    @BindView(R.id.food_input_icon) ImageView icon;
    @BindView(R.id.food_input_row) ViewGroup inputRow;
    @BindView(R.id.food_input_value_calculated) TextView valueCalculated;
    @BindView(R.id.food_input_value_input) StickyHintInput valueInput;
    @BindView(R.id.food_input_list) RecyclerView foodList;

    private FoodInputListAdapter adapter;
    private Meal meal;

    private boolean showIcon;

    public FoodInputView(Context context) {
        super(context);
        initLayout();
    }

    public FoodInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttributes(attrs);
        initLayout();
    }

    public FoodInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttributes(attrs);
        initLayout();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Events.register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Events.unregister(this);
    }

    private void getAttributes(AttributeSet attributeSet) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.FoodInputView);
        try {
            showIcon = typedArray.getBoolean(R.styleable.FoodInputView_showIcon, false);
        } finally {
            typedArray.recycle();
        }
    }

    private void initLayout() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_food_input, this);

        if (!isInEditMode()) {
            ButterKnife.bind(this);
            meal = new Meal();

            icon.setVisibility(showIcon ? VISIBLE : GONE);
            inputRow.setMinimumHeight(getResources().getDimensionPixelSize(showIcon ? R.dimen.height_element_large : R.dimen.height_element));

            valueInput.setHint(PreferenceStore.getInstance().getUnitName(Category.MEAL));

            adapter = new FoodInputListAdapter(getContext());
            foodList.setLayoutManager(new LinearLayoutManager(getContext()));
            foodList.addItemDecoration(new VerticalDividerItemDecoration(getContext()));
            foodList.setAdapter(adapter);

            invalidateLayout();
        }
    }

    private void invalidateLayout() {
        boolean hasFoodEaten = adapter.hasInput();
        if (hasFoodEaten) {
            valueCalculated.setVisibility(VISIBLE);
            float carbohydrates = adapter.getTotalCarbohydrates();
            float meal = PreferenceStore.getInstance().formatDefaultToCustomUnit(Category.MEAL, carbohydrates);
            valueCalculated.setText(String.format("%s   +", FloatUtils.parseFloat(meal)));
        } else {
            valueCalculated.setVisibility(GONE);
            valueCalculated.setText(null);
        }
    }

    public void setupWithMeal(Meal meal) {
        this.meal = meal;
        this.valueInput.setText(meal.getValuesForUI()[0]);
        addItems(meal.getFoodEaten());
    }

    public boolean isValid() {
        boolean isValid = true;

        String input = valueInput.getText().trim();

        if (StringUtils.isBlank(input) && !adapter.hasInput()) {
            valueInput.setError(getContext().getString(R.string.validator_value_empty));
            isValid = false;
        } else if (!StringUtils.isBlank(input)) {
            isValid = PreferenceStore.getInstance().isValueValid(valueInput.getEditText(), Category.MEAL);
        }
        return isValid;
    }

    public Meal getMeal() {
        if (isValid()) {
            meal.setValues(valueInput.getText().length() > 0 ?
                    PreferenceStore.getInstance().formatCustomToDefaultUnit(
                            meal.getCategory(),
                            FloatUtils.parseNumber(valueInput.getText())) : 0);
            List<FoodEaten> foodEatenCache = new ArrayList<>();
            for (FoodEaten foodEaten : adapter.getItems()) {
                if (foodEaten.getAmountInGrams() > 0) {
                    foodEatenCache.add(foodEaten);
                }
            }
            meal.setFoodEatenCache(foodEatenCache);
            return meal;
        } else {
            return null;
        }
    }

    public void addItem(FoodEaten foodEaten) {
        if (foodEaten != null) {
            int position = 0;
            adapter.addItem(position, foodEaten);
            adapter.notifyItemInserted(position);
            invalidateLayout();
        }
    }

    public void addItem(Food food) {
        if (food != null) {
            FoodEaten foodEaten = new FoodEaten();
            foodEaten.setFood(food);
            foodEaten.setMeal(meal);
            addItem(foodEaten);
        }
    }

    public void addItems(ForeignCollection<FoodEaten> foodEatenList) {
        if (foodEatenList != null && foodEatenList.size() > 0) {
            int oldCount = adapter.getItemCount();
            for (FoodEaten foodEaten : foodEatenList) {
                adapter.addItem(foodEaten);
            }
            adapter.notifyItemRangeInserted(oldCount, adapter.getItemCount());
            invalidateLayout();
        }
    }

    public void clear() {
        int itemCount = adapter.getItemCount();
        adapter.clear();
        adapter.notifyItemRangeRemoved(0, itemCount);
        invalidateLayout();
    }

    public void removeItem(int position) {
        adapter.removeItem(position);
        adapter.notifyItemRemoved(position);
        invalidateLayout();
    }

    public void updateItem(FoodEaten foodEaten, int position) {
        adapter.updateItem(position, foodEaten);
        adapter.notifyItemChanged(position);
        invalidateLayout();
    }

    public float getTotalCarbohydrates() {
        return getInputCarbohydrates() + getCalculatedCarbohydrates();
    }

    public float getInputCarbohydrates() {
        float input = FloatUtils.parseNumber(valueInput.getText());
        return PreferenceStore.getInstance().formatCustomToDefaultUnit(Category.MEAL, input);
    }

    public float getCalculatedCarbohydrates() {
        return adapter.getTotalCarbohydrates();
    }

    public List<FoodEaten> getFoodEatenList() {
        return adapter.getItems();
    }

    @OnClick(R.id.food_input_button)
    public void searchForFood() {
        Intent intent = new Intent(getContext(), FoodSearchActivity.class);
        intent.putExtra(FoodSearchFragment.FINISH_ON_SELECTION, true);
        getContext().startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FoodSelectedEvent event) {
        addItem(event.context);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FoodEatenUpdatedEvent event) {
        updateItem(event.context, event.position);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FoodEatenRemovedEvent event) {
        removeItem(event.position);
    }
}
