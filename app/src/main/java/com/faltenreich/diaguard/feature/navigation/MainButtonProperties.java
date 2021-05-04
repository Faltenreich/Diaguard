package com.faltenreich.diaguard.feature.navigation;

import android.view.View;

import androidx.annotation.DrawableRes;

import com.faltenreich.diaguard.R;

public class MainButtonProperties {

    @DrawableRes private final int iconDrawableResId;
    private final View.OnClickListener onClickListener;
    private final boolean slideOutOnScroll;

    public MainButtonProperties(
        @DrawableRes int iconDrawableResId,
        View.OnClickListener onClickListener,
        boolean slideOutOnScroll
    ) {
        this.iconDrawableResId = iconDrawableResId;
        this.onClickListener = onClickListener;
        this.slideOutOnScroll = slideOutOnScroll;
    }

    public MainButtonProperties(
        @DrawableRes int iconDrawableResId,
        View.OnClickListener onClickListener
    ) {
        this(iconDrawableResId, onClickListener, true);
    }

    public int getIconDrawableResId() {
        return iconDrawableResId;
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public boolean slideOutOnScroll() {
        return slideOutOnScroll;
    }

    public static MainButtonProperties addButton(View.OnClickListener onClickListener, boolean slideOut) {
        return new MainButtonProperties(R.drawable.fab_add, onClickListener, slideOut);
    }

    public static MainButtonProperties confirmButton(View.OnClickListener onClickListener, boolean slideOut) {
        return new MainButtonProperties(R.drawable.ic_done, onClickListener, slideOut);
    }
}
