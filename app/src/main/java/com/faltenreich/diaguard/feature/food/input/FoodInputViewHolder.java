package com.faltenreich.diaguard.feature.food.input;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.data.database.entity.FoodEaten;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;
import com.faltenreich.diaguard.shared.event.Events;
import com.faltenreich.diaguard.shared.event.ui.FoodEatenRemovedEvent;
import com.faltenreich.diaguard.shared.event.ui.FoodEatenUpdatedEvent;
import com.faltenreich.diaguard.shared.view.ViewUtils;
import com.faltenreich.diaguard.shared.view.recyclerview.viewholder.BaseViewHolder;
import com.faltenreich.diaguard.shared.view.resource.ColorUtils;

import butterknife.BindView;

/**
 * Created by Faltenreich on 03.10.2016.
 */

class FoodInputViewHolder extends BaseViewHolder<FoodEaten> {

    @BindView(R.id.food_name)
    TextView name;
    @BindView(R.id.food_carbohydrates)
    TextView value;
    @BindView(R.id.food_amount)
    AppCompatButton amount;
    @BindView(R.id.food_delete)
    ImageView delete;

    FoodInputViewHolder(ViewGroup parent) {
        super(parent, R.layout.list_item_measurement_meal_food_item);
        amount.setOnClickListener(view -> showNumberPicker());
        delete.setOnClickListener(view -> deleteFood());
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onBind(FoodEaten item) {
        Food food = item.getFood();

        name.setText(food.getName());
        value.setText(String.format("%s %s",
            food.getValueForUi(),
            PreferenceStore.getInstance().getLabelForMealPer100g(getContext()))
        );
        delete.setContentDescription(String.format(getContext().getString(R.string.remove_placeholder), food.getName()));

        boolean isSet = item.getAmountInGrams() > 0;
        String text = isSet ?
            String.format("%s %s", FloatUtils.parseFloat(item.getAmountInGrams()), getContext().getString(R.string.grams_milliliters_acronym)) :
            getContext().getString(R.string.amount);
        int backgroundColor = isSet ? ColorUtils.getBackgroundTertiary(getContext()) : ColorUtils.getPrimaryColor(getContext());
        int textColor = isSet ? ColorUtils.getTextColorPrimary(getContext()) : Color.WHITE;
        amount.setText(text);
        amount.setSupportBackgroundTintList(ColorStateList.valueOf(backgroundColor));
        amount.setTextColor(textColor);
    }

    private void showNumberPicker() {
        if (getContext() instanceof AppCompatActivity) {
            ViewUtils.showNumberPicker((AppCompatActivity) getContext(), R.string.grams_milliliters_acronym, getAmountFromButton(), 1, 10000, (reference, number, decimal, isNegative, fullNumber) -> {
                FoodEaten foodEaten = getItem();
                foodEaten.setAmountInGrams(number.floatValue());
                Events.post(new FoodEatenUpdatedEvent(foodEaten, getAdapterPosition()));
            });
        }
    }

    private int getAmountFromButton() {
        String label = amount.getText().toString();
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
