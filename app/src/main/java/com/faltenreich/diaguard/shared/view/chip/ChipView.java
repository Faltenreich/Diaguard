package com.faltenreich.diaguard.shared.view.chip;

import android.content.Context;

import com.google.android.material.chip.Chip;

public class ChipView extends Chip {

    public ChipView(Context context) {
        super(context);
        // Workaround: Required to support chip spacing
        // https://github.com/material-components/material-components-android/issues/1004
        setEnsureMinTouchTargetSize(false);
    }
}
