package com.faltenreich.diaguard.ui.view.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.codetroopers.betterpickers.numberpicker.NumberPickerBuilder;
import com.codetroopers.betterpickers.numberpicker.NumberPickerDialogFragment;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.ui.activity.BaseActivity;
import com.faltenreich.diaguard.util.NumberUtils;

import java.math.BigDecimal;
import java.math.BigInteger;

import butterknife.BindView;

/**
 * Created by Faltenreich on 03.10.2016.
 */

public class FoodEditViewHolder extends BaseViewHolder<Food> {

    @BindView(R.id.food_image) ImageView image;
    @BindView(R.id.food_name) TextView name;
    @BindView(R.id.food_amount) Button amount;

    public FoodEditViewHolder(View view) {
        super(view);
    }

    @Override
    protected void bindData() {
        amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getContext() instanceof BaseActivity) {
                    BaseActivity activity = (BaseActivity) getContext();
                    NumberPickerBuilder npb = new NumberPickerBuilder()
                            .setFragmentManager(activity.getSupportFragmentManager())
                            .setStyleResId(R.style.BetterPickersDialogFragment_Light)
                            .setLabelText(getContext().getString(R.string.grams))
                            .setPlusMinusVisibility(View.GONE)
                            .setDecimalVisibility(View.GONE)
                            .addNumberPickerDialogHandler(new NumberPickerDialogFragment.NumberPickerDialogHandlerV2() {
                                @Override
                                public void onDialogNumberSet(int reference, BigInteger number, double decimal, boolean isNegative, BigDecimal fullNumber) {
                                    setAmountToButton(number.intValue());
                                }
                            });
                    int currentAmount = getAmountFromButton();
                    if (currentAmount > 0) {
                        npb.setCurrentNumber(currentAmount);
                    }
                    npb.show();
                }
            }
        });
        setAmountToButton(0);
    }

    private void setAmountToButton(int number) {
        amount.setText(String.format("%d %s", number, getContext().getString(R.string.grams)));
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
