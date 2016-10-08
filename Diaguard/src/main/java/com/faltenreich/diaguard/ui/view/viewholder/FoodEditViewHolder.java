package com.faltenreich.diaguard.ui.view.viewholder;

import android.content.res.ColorStateList;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.TextView;

import com.codetroopers.betterpickers.numberpicker.NumberPickerBuilder;
import com.codetroopers.betterpickers.numberpicker.NumberPickerDialogFragment;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.event.Events;
import com.faltenreich.diaguard.event.ui.FoodRemovedEvent;
import com.faltenreich.diaguard.ui.view.TintImageView;
import com.faltenreich.diaguard.util.NumberUtils;

import java.math.BigDecimal;
import java.math.BigInteger;

import butterknife.BindView;

/**
 * Created by Faltenreich on 03.10.2016.
 */

public class FoodEditViewHolder extends BaseViewHolder<Food> {

    @BindView(R.id.food_name) TextView name;
    @BindView(R.id.food_carbohydrates) TextView carbohydrates;
    @BindView(R.id.food_amount) AppCompatButton amount;
    @BindView(R.id.food_delete) TintImageView delete;

    public FoodEditViewHolder(View view) {
        super(view);
    }

    @Override
    protected void bindData() {
        final Food food = getListItem();

        this.name.setText(food.getName());
        this.carbohydrates.setText(String.format("%d %s", (int) food.getCarbohydrates(), getContext().getString(R.string.carbohydrates_per_100g)));

        this.amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getContext() instanceof AppCompatActivity) {
                    showNumberPicker((AppCompatActivity) getContext());
                }
            }
        });

        this.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Events.post(new FoodRemovedEvent(food));
            }
        });
    }

    private void showNumberPicker(AppCompatActivity activity) {
        NumberPickerBuilder numberPicker = new NumberPickerBuilder()
                .setFragmentManager(activity.getSupportFragmentManager())
                .setStyleResId(R.style.BetterPickersDialogFragment_Light)
                .setLabelText(getContext().getString(R.string.grams))
                .setPlusMinusVisibility(View.GONE)
                .setDecimalVisibility(View.GONE)
                .setMaxNumber(BigDecimal.valueOf(10000))
                .setMinNumber(BigDecimal.valueOf(1))
                .addNumberPickerDialogHandler(new NumberPickerDialogFragment.NumberPickerDialogHandlerV2() {
                    @Override
                    public void onDialogNumberSet(int reference, BigInteger number, double decimal, boolean isNegative, BigDecimal fullNumber) {
                        setAmountToButton(number.intValue());
                    }
                });
        int currentAmount = getAmountFromButton();
        if (currentAmount > 0) {
            numberPicker.setCurrentNumber(currentAmount);
        }
        numberPicker.show();
    }

    private void setAmountToButton(int number) {
        this.amount.setText(String.format("%d %s", number, getContext().getString(R.string.grams)));
        this.amount.setSupportBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.gray_light)));
        this.amount.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
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
