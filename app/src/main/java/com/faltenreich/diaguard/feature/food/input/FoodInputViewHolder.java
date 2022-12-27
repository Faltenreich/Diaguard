package com.faltenreich.diaguard.feature.food.input;

import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.ListItemMeasurementMealFoodItemBinding;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.data.database.entity.FoodEaten;
import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;
import com.faltenreich.diaguard.shared.event.Events;
import com.faltenreich.diaguard.shared.event.ui.FoodEatenRemovedEvent;
import com.faltenreich.diaguard.shared.event.ui.FoodEatenUpdatedEvent;
import com.faltenreich.diaguard.shared.view.picker.NumberPickerDialog;
import com.faltenreich.diaguard.shared.view.recyclerview.viewholder.BaseViewHolder;

/**
 * Created by Faltenreich on 03.10.2016.
 */

class FoodInputViewHolder extends BaseViewHolder<ListItemMeasurementMealFoodItemBinding, FoodEaten> {

    FoodInputViewHolder(ViewGroup parent) {
        super(parent, R.layout.list_item_measurement_meal_food_item);
        initLayout();
    }

    @Override
    protected ListItemMeasurementMealFoodItemBinding createBinding(View view) {
        return ListItemMeasurementMealFoodItemBinding.bind(view);
    }

    private void initLayout() {
        getBinding().amountButton.setOnClickListener(view -> showNumberPicker());
    }

    @Override
    protected void onBind(FoodEaten item) {
        Food food = item.getFood();
        getBinding().nameLabel.setText(food.getName());
        getBinding().valueLabel.setText(String.format("%s %s",
            food.getValueForUi(),
            PreferenceStore.getInstance().getLabelForMealPer100g(getContext()))
        );
        getBinding().amountButton.setText(item.isValid() ?
            String.format("%s %s", FloatUtils.parseFloat(item.getAmountInGrams()), getContext().getString(R.string.grams_milliliters_acronym)) :
            getContext().getString(R.string.amount)
        );
    }

    // TODO: Add option to delete food from number picker, e.g. via bottom sheet
    private void showNumberPicker() {
        if (getContext() instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) getContext();
            new NumberPickerDialog(getContext(), R.string.grams_milliliters_acronym, getAmountFromButton(), 1, 10000, (number) -> {
                FoodEaten foodEaten = getItem();
                foodEaten.setAmountInGrams(number.floatValue());
                Events.post(new FoodEatenUpdatedEvent(foodEaten, getBindingAdapterPosition()));
            }).show(activity.getSupportFragmentManager());
        }
    }

    private int getAmountFromButton() {
        String label = getBinding().amountButton.getText().toString();
        if (label.length() > 0) {
            String numberLabel = label.substring(0, label.indexOf(" "));
            try {
                return (int) FloatUtils.parseNumber(numberLabel);
            } catch (NumberFormatException exception) {
                return 0;
            }
        } else {
            return 0;
        }
    }

    private void deleteFood() {
        Events.post(new FoodEatenRemovedEvent(getItem(), getAdapterPosition()));
    }
}
