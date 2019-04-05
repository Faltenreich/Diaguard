package com.faltenreich.diaguard.ui.list.viewholder;

import android.content.res.ColorStateList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.data.entity.FoodEaten;
import com.faltenreich.diaguard.event.Events;
import com.faltenreich.diaguard.event.ui.FoodEatenRemovedEvent;
import com.faltenreich.diaguard.event.ui.FoodEatenUpdatedEvent;
import com.faltenreich.diaguard.util.Helper;
import com.faltenreich.diaguard.util.NumberUtils;
import com.faltenreich.diaguard.util.ResourceUtils;
import com.faltenreich.diaguard.util.ViewUtils;

import butterknife.BindView;

/**
 * Created by Faltenreich on 03.10.2016.
 */

public class FoodEditViewHolder extends BaseViewHolder<FoodEaten> {

    @BindView(R.id.food_name) TextView name;
    @BindView(R.id.food_carbohydrates) TextView value;
    @BindView(R.id.food_amount) AppCompatButton amount;
    @BindView(R.id.food_delete) ImageView delete;

    public FoodEditViewHolder(View view) {
        super(view);
    }

    @Override
    protected void bindData() {
        final FoodEaten foodEaten = getListItem();
        final Food food = foodEaten.getFood();

        name.setText(food.getName());
        value.setText(String.format("%s %s",
                food.getValueForUi(),
                PreferenceHelper.getInstance().getLabelForMealPer100g(getContext())));

        amount.setOnClickListener(view -> {
            if (getContext() instanceof AppCompatActivity) {
                showNumberPicker((AppCompatActivity) getContext());
            }
        });

        delete.setOnClickListener(view -> Events.post(new FoodEatenRemovedEvent(foodEaten, getAdapterPosition())));
        delete.setContentDescription(String.format(getContext().getString(R.string.remove_placeholder), food.getName()));

        updateUi();
    }

    private void showNumberPicker(AppCompatActivity activity) {
        ViewUtils.showNumberPicker(activity, R.string.grams_milliliters_acronym, getAmountFromButton(), 1, 10000, (reference, number, decimal, isNegative, fullNumber) -> {
            FoodEaten foodEaten = getListItem();
            foodEaten.setAmountInGrams(number.floatValue());
            Events.post(new FoodEatenUpdatedEvent(foodEaten, getAdapterPosition()));
        });
    }

    @SuppressWarnings("RestrictedApi")
    private void updateUi() {
        FoodEaten foodEaten = getListItem();
        boolean isSet = foodEaten.getAmountInGrams() > 0;
        String text = isSet ?
                String.format("%s %s", Helper.parseFloat(getListItem().getAmountInGrams()), getContext().getString(R.string.grams_milliliters_acronym)) :
                getContext().getString(R.string.amount);
        int backgroundColor = isSet ? ResourceUtils.getBackgroundTertiary(getContext()) : ResourceUtils.getPrimaryColor(getContext());
        int textColor = isSet ? ResourceUtils.getTextColorPrimary(getContext()) : Color.WHITE;
        amount.setText(text);
        amount.setSupportBackgroundTintList(ColorStateList.valueOf(backgroundColor));
        amount.setTextColor(textColor);
    }

    private int getAmountFromButton() {
        String label = amount.getText().toString();
        if (label.length() > 0) {
            String numberLabel = label.substring(0, label.indexOf(" "));
            try {
                return (int) NumberUtils.parseNumber(numberLabel);
            } catch (NumberFormatException exception) {
                return 0;
            }
        } else {
            return 0;
        }
    }
}
