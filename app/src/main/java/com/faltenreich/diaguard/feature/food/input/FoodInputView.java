package com.faltenreich.diaguard.feature.food.input;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.ViewFoodInputBinding;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.data.database.entity.FoodEaten;
import com.faltenreich.diaguard.shared.data.database.entity.Meal;
import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;
import com.faltenreich.diaguard.shared.data.primitive.StringUtils;
import com.faltenreich.diaguard.shared.event.Events;
import com.faltenreich.diaguard.shared.event.ui.FoodEatenRemovedEvent;
import com.faltenreich.diaguard.shared.event.ui.FoodEatenUpdatedEvent;
import com.faltenreich.diaguard.shared.event.ui.FoodSearchEvent;
import com.faltenreich.diaguard.shared.event.ui.FoodSearchedEvent;
import com.faltenreich.diaguard.shared.view.ViewBindable;
import com.faltenreich.diaguard.shared.view.edittext.StickyHintInputView;
import com.faltenreich.diaguard.shared.view.recyclerview.decoration.VerticalDividerItemDecoration;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Collection;
import java.util.List;

/**
 * Created by Faltenreich on 13.10.2016.
 */

public class FoodInputView extends LinearLayout implements ViewBindable<ViewFoodInputBinding>, TextWatcher {

    private ViewFoodInputBinding binding;

    private ImageView inputIconImageView;
    private ViewGroup inputLayout;
    private TextView calculatedValueLabel;
    private StickyHintInputView inputValueInputField;
    private RecyclerView foodListView;
    private Button addButton;

    private FoodInputListAdapter foodListAdapter;
    private boolean showIcon;
    private Meal meal;

    public FoodInputView(Context context) {
        super(context);
        init(null);
    }

    public FoodInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public FoodInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @Override
    public ViewFoodInputBinding getBinding() {
        return binding;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Events.register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        inputValueInputField.getEditText().removeTextChangedListener(this);
        Events.unregister(this);
        super.onDetachedFromWindow();
    }

    private void init(@Nullable AttributeSet attributeSet) {
        if (attributeSet != null) {
            getAttributes(attributeSet);
        }
        if (!isInEditMode()) {
            bindView();
            initData();
            initLayout();
            invalidateLayout();
        }
    }

    private void getAttributes(@NonNull AttributeSet attributeSet) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.FoodInputView);
        try {
            showIcon = typedArray.getBoolean(R.styleable.FoodInputView_showIcon, false);
        } finally {
            typedArray.recycle();
        }
    }

    private void bindView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_food_input, this);
        binding = ViewFoodInputBinding.bind(this);

        inputIconImageView = getBinding().inputIconImageView;
        inputLayout = getBinding().inputLayout;
        calculatedValueLabel = getBinding().calculatedValueLabel;
        inputValueInputField = getBinding().inputValueInputField;
        foodListView = getBinding().foodListView;
        addButton = getBinding().addButton;
    }

    public StickyHintInputView getInputField() {
        return inputValueInputField;
    }

    private void initData() {
        if (meal == null) {
            meal = new Meal();
        }
    }

    private void initLayout() {
        addButton.setOnClickListener((view) -> searchForFood());

        inputIconImageView.setVisibility(showIcon ? VISIBLE : GONE);
        inputLayout.setMinimumHeight(getResources().getDimensionPixelSize(showIcon ? R.dimen.height_element_large : R.dimen.height_element));

        inputValueInputField.setHint(PreferenceStore.getInstance().getUnitName(Category.MEAL));
        inputValueInputField.getEditText().addTextChangedListener(this);

        foodListAdapter = new FoodInputListAdapter(getContext());
        foodListView.setLayoutManager(new LinearLayoutManager(getContext()));
        foodListView.addItemDecoration(new VerticalDividerItemDecoration(getContext()));
        foodListView.setAdapter(foodListAdapter);
    }

    private void invalidateLayout() {
        foodListView.setVisibility(foodListAdapter.hasFood() ? VISIBLE : GONE);

        if (foodListAdapter.hasFoodEaten()) {
            calculatedValueLabel.setVisibility(VISIBLE);
            float carbohydrates = foodListAdapter.getTotalCarbohydrates();
            float meal = PreferenceStore.getInstance().formatDefaultToCustomUnit(Category.MEAL, carbohydrates);
            calculatedValueLabel.setText(String.format("%s   +", FloatUtils.parseFloat(meal)));
        } else {
            calculatedValueLabel.setVisibility(GONE);
            calculatedValueLabel.setText(null);
        }

        meal.setFoodEatenCache(foodListAdapter.getItems());
    }

    @NonNull
    public Meal getMeal() {
        return meal;
    }

    // FIXME: Previously cached but removed food eaten gets added obsoletely
    public void setMeal(Meal meal) {
        this.meal = meal;
        this.inputValueInputField.setText(meal.getValuesForUI()[0]);
        if (meal.getFoodEatenCache().isEmpty()) {
            addItems(meal.getFoodEaten());
        } else {
            addItems(meal.getFoodEatenCache());
        }
    }

    public boolean isValid() {
        boolean isValid = true;

        String input = inputValueInputField.getText().trim();

        if (StringUtils.isBlank(input) && !foodListAdapter.hasFoodEaten()) {
            inputValueInputField.setError(getContext().getString(R.string.validator_value_empty));
            isValid = false;
        } else if (!StringUtils.isBlank(input)) {
            isValid = PreferenceStore.getInstance().isValueValid(inputValueInputField.getEditText(), Category.MEAL);
        }
        return isValid;
    }

    private void addItem(FoodEaten foodEaten) {
        if (foodEaten != null) {
            int position = 0;
            foodListAdapter.addItem(position, foodEaten);
            foodListAdapter.notifyItemInserted(position);
            invalidateLayout();
        }
    }

    private void addItem(Food food) {
        if (food != null) {
            FoodEaten foodEaten = new FoodEaten();
            foodEaten.setFood(food);
            foodEaten.setMeal(meal);
            addItem(foodEaten);
        }
    }

    private void addItems(Collection<FoodEaten> foodEatenList) {
        if (foodEatenList != null && foodEatenList.size() > 0) {
            int oldCount = foodListAdapter.getItemCount();
            for (FoodEaten foodEaten : foodEatenList) {
                foodListAdapter.addItem(foodEaten);
            }
            foodListAdapter.notifyItemRangeInserted(oldCount, foodListAdapter.getItemCount());
            invalidateLayout();
        }
    }

    public void clear() {
        int itemCount = foodListAdapter.getItemCount();
        foodListAdapter.clear();
        foodListAdapter.notifyItemRangeRemoved(0, itemCount);
        invalidateLayout();
    }

    public void removeItem(int position) {
        foodListAdapter.removeItem(position);
        foodListAdapter.notifyItemRemoved(position);
        invalidateLayout();
    }

    private void updateItem(FoodEaten foodEaten, int position) {
        foodListAdapter.updateItem(position, foodEaten);
        foodListAdapter.notifyItemChanged(position);
        invalidateLayout();
    }

    public float getTotalCarbohydrates() {
        return getInputCarbohydrates() + getCalculatedCarbohydrates();
    }

    public float getInputCarbohydrates() {
        float input = FloatUtils.parseNumber(inputValueInputField.getText());
        return PreferenceStore.getInstance().formatCustomToDefaultUnit(Category.MEAL, input);
    }

    public float getCalculatedCarbohydrates() {
        return foodListAdapter.getTotalCarbohydrates();
    }

    public List<FoodEaten> getFoodEatenList() {
        return foodListAdapter.getItems();
    }

    private void searchForFood() {
        Events.post(new FoodSearchEvent());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String input = editable.toString();
        meal.setValues(input.length() > 0 ?
            PreferenceStore.getInstance().formatCustomToDefaultUnit(
                meal.getCategory(),
                FloatUtils.parseNumber(input)
            ) : 0);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FoodSearchedEvent event) {
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
