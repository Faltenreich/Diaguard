package com.faltenreich.diaguard.shared.view.dialog;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

public class DialogConfig {

    @LayoutRes
    private final int layoutResId;
    private final String title;
    @Nullable
    private final String description;

    private final DialogButton positiveButton;
    @Nullable
    private final DialogButton negativeButton;
    @Nullable
    private final DialogButton neutralButton;

    public DialogConfig(
        @LayoutRes int layoutResId,
        String title,
        @Nullable String description,
        DialogButton positiveButton,
        @Nullable DialogButton negativeButton,
        @Nullable DialogButton neutralButton
    ) {
        this.layoutResId = layoutResId;
        this.title = title;
        this.description = description;
        this.positiveButton = positiveButton;
        this.negativeButton = negativeButton;
        this.neutralButton = neutralButton;
    }

    public int getLayoutResId() {
        return layoutResId;
    }

    public String getTitle() {
        return title;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public DialogButton getPositiveButton() {
        return positiveButton;
    }

    @Nullable
    public DialogButton getNegativeButton() {
        return negativeButton;
    }

    @Nullable
    public DialogButton getNeutralButton() {
        return neutralButton;
    }
}
