package com.faltenreich.diaguard.shared.view.search;

import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

public class SearchViewAction {

    private int iconRes;
    private int contentDescriptionRes;
    private Callback callback;

    public SearchViewAction(
        @DrawableRes int iconRes,
        @StringRes int contentDescriptionRes,
        Callback callback
    ) {
        this.iconRes = iconRes;
        this.contentDescriptionRes = contentDescriptionRes;
        this.callback = callback;
    }

    @DrawableRes
    int getIconRes() {
        return iconRes;
    }

    @StringRes
    int getContentDescriptionRes() {
        return contentDescriptionRes;
    }

    Callback getCallback() {
        return callback;
    }

    public interface Callback {
        void onAction(View view);
    }
}
