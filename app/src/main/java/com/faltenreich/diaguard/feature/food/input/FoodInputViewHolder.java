package com.faltenreich.diaguard.feature.food.input;

import android.view.View;
import android.view.ViewGroup;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.ListItemMeasurementMealFoodItemBinding;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.data.database.entity.FoodEaten;
import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;
import com.faltenreich.diaguard.shared.event.Events;
import com.faltenreich.diaguard.shared.event.ui.FoodEatenRemovedEvent;
import com.faltenreich.diaguard.shared.view.edittext.StickyHintInputView;
import com.faltenreich.diaguard.shared.view.recyclerview.viewholder.BaseViewHolder;

/**
 * Created by Faltenreich on 03.10.2016.
 */

class FoodInputViewHolder extends BaseViewHolder<ListItemMeasurementMealFoodItemBinding, FoodEaten> {

    FoodInputViewHolder(ViewGroup parent) {
        super(parent, R.layout.list_item_measurement_meal_food_item);
        getBinding().amountInput.setEndIconOnClickListener(view -> deleteFood());
    }

    @Override
    protected ListItemMeasurementMealFoodItemBinding createBinding(View view) {
        return ListItemMeasurementMealFoodItemBinding.bind(view);
    }

    @Override
    protected void onBind(FoodEaten item) {
        Food food = item.getFood();
        StickyHintInputView inputView = getBinding().amountInput;
        inputView.setText(item.isValid() ? FloatUtils.parseFloat(item.getAmountInGrams()) : null);
        inputView.setHint(food.getName());
        inputView.setHelperText(String.format("%s %s",
            food.getValueForUi(),
            PreferenceStore.getInstance().getLabelForMealPer100g(getContext()))
        );
    }

    private int getAmountFromButton() {
        String label = getBinding().amountInput.getText();
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
