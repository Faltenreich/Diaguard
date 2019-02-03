package com.faltenreich.diaguard.ui.view;

import androidx.annotation.DrawableRes;
import android.view.View;

import com.faltenreich.diaguard.R;

public class MainButtonProperties {

    @DrawableRes
    private int iconDrawableResId;
    private View.OnClickListener onClickListener;
    private boolean slideOut;

    public MainButtonProperties(@DrawableRes int iconDrawableResId, View.OnClickListener onClickListener, boolean slideOut) {
        this.iconDrawableResId = iconDrawableResId;
        this.onClickListener = onClickListener;
        this.slideOut = slideOut;
    }

    public MainButtonProperties(@DrawableRes int iconDrawableResId, View.OnClickListener onClickListener) {
        this(iconDrawableResId, onClickListener, true);
    }

    public int getIconDrawableResId() {
        return iconDrawableResId;
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public boolean slideOut() {
        return slideOut;
    }

    public static MainButtonProperties addButton(View.OnClickListener onClickListener, boolean slideOut) {
        return new MainButtonProperties(R.drawable.fab_add, onClickListener, slideOut);
    }

    public static MainButtonProperties addButton(View.OnClickListener onClickListener) {
        return addButton(onClickListener, true);
    }

    public static MainButtonProperties confirmButton(View.OnClickListener onClickListener, boolean slideOut) {
        return new MainButtonProperties(R.drawable.ic_action_done, onClickListener, slideOut);
    }

    public static MainButtonProperties confirmButton(View.OnClickListener onClickListener) {
        return confirmButton(onClickListener, true);
    }
}
