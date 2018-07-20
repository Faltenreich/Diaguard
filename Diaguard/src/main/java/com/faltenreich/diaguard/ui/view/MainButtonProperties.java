package com.faltenreich.diaguard.ui.view;

import android.support.annotation.DrawableRes;
import android.view.View;

import com.faltenreich.diaguard.R;

public class MainButtonProperties {

    @DrawableRes
    private int iconDrawableResId;

    private View.OnClickListener onClickListener;

    public MainButtonProperties(@DrawableRes int iconDrawableResId, View.OnClickListener onClickListener) {
        this.iconDrawableResId = iconDrawableResId;
        this.onClickListener = onClickListener;
    }

    public int getIconDrawableResId() {
        return iconDrawableResId;
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public static MainButtonProperties addButton(View.OnClickListener onClickListener) {
        return new MainButtonProperties(R.drawable.fab_add, onClickListener);
    }

    public static MainButtonProperties confirmButton(View.OnClickListener onClickListener) {
        return new MainButtonProperties(R.drawable.ic_action_done, onClickListener);
    }
}
