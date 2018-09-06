package com.faltenreich.diaguard.ui.view;

import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

public class DialogButton {

    @StringRes private int labelResId;
    @Nullable private DialogButtonListener listener;

    public DialogButton(@StringRes int labelResId, @Nullable DialogButtonListener listener) {
        this.labelResId = labelResId;
        this.listener = listener;
    }

    public DialogButton(@StringRes int labelResId) {
        this.labelResId = labelResId;
        this.listener = null;
    }

    public int getLabelResId() {
        return labelResId;
    }

    @Nullable
    public DialogButtonListener getListener() {
        return listener;
    }

    public interface DialogButtonListener {
        void onClick();
    }
}
