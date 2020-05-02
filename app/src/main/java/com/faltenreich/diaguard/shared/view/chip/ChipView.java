package com.faltenreich.diaguard.shared.view.chip;

import android.content.Context;
import android.util.AttributeSet;

import com.google.android.material.chip.Chip;

public class ChipView extends Chip {

    public ChipView(Context context) {
        super(context);
        init();
    }

    public ChipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChipView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }
}
