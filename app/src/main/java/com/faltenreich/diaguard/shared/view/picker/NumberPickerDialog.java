package com.faltenreich.diaguard.shared.view.picker;

import android.content.Context;
import android.view.View;

import androidx.annotation.StringRes;
import androidx.fragment.app.FragmentManager;

import com.codetroopers.betterpickers.numberpicker.NumberPickerBuilder;
import com.faltenreich.diaguard.R;

import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberPickerDialog {

    private final String title;
    private final int initialValue;
    private final int minValue;
    private final int maxValue;
    private final Listener listener;

    public NumberPickerDialog(
        Context context,
        @StringRes int titleResourceId,
        int initialValue,
        int minValue,
        int maxValue,
        Listener listener
    ) {
        this.title = context.getString(titleResourceId);
        this.initialValue = initialValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.listener = listener;
    }

    public void show(FragmentManager fragmentManager) {
        new NumberPickerBuilder()
            .setFragmentManager(fragmentManager)
            .setStyleResId(R.style.NumberPicker)
            .setLabelText(title)
            .setPlusMinusVisibility(View.GONE)
            .setDecimalVisibility(View.GONE)
            .setMaxNumber(BigDecimal.valueOf(maxValue))
            .setMinNumber(BigDecimal.valueOf(minValue))
            .addNumberPickerDialogHandler((reference, number, decimal, isNegative, fullNumber) -> listener.onNumberPicked(number))
            .setCurrentNumber(initialValue > 0 ? initialValue : null)
            .show();
    }

    public interface Listener {

        void onNumberPicked(BigInteger number);
    }
}
