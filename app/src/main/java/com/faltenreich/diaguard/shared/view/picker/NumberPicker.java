package com.faltenreich.diaguard.shared.view.picker;

import androidx.fragment.app.FragmentManager;

public class NumberPicker implements NumberPicking {

    private final String title;
    private final int initialValue;
    private final int minValue;
    private final int maxValue;
    private final Listener listener;

    public NumberPicker(
        String title,
        int initialValue,
        int minValue,
        int maxValue,
        Listener listener
    ) {
        this.title = title;
        this.initialValue = initialValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.listener = listener;
    }

    @Override
    public void show(FragmentManager fragmentManager) {
        NumberPickerBottomSheetDialogFragment.newInstance(
            title,
            initialValue,
            minValue,
            maxValue,
            listener
        ).show(fragmentManager);
    }

    /*
    @Deprecated
    public void showOld(FragmentManager fragmentManager) {
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
    */
}
