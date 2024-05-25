package com.faltenreich.diaguard.feature.navigation;

import android.view.View;

import androidx.annotation.DrawableRes;

import com.faltenreich.diaguard.R;

public class FabProperties {

    @DrawableRes private final int iconDrawableResId;
    private final View.OnClickListener onClickListener;

    public FabProperties(
        @DrawableRes int iconDrawableResId,
        View.OnClickListener onClickListener
    ) {
        this.iconDrawableResId = iconDrawableResId;
        this.onClickListener = onClickListener;
    }

    public int getIconDrawableResId() {
        return iconDrawableResId;
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public static FabProperties addButton(View.OnClickListener onClickListener) {
        return new FabProperties(R.drawable.ic_add_fab, onClickListener);
    }

    public static FabProperties confirmButton(View.OnClickListener onClickListener) {
        return new FabProperties(R.drawable.ic_done, onClickListener);
    }
}
