package com.faltenreich.diaguard.feature.navigation;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.MenuRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

public class ToolbarProperties {

    private static final String TAG = ToolbarProperties.class.getSimpleName();

    private final String title;
    private final Integer menuResId;
    private final View.OnClickListener onClickListener;

    private ToolbarProperties(
        String title,
        @MenuRes Integer menuResId,
        View.OnClickListener onClickListener
    ) {
        this.title = title;
        this.menuResId = menuResId;
        this.onClickListener = onClickListener;
    }

    public String getTitle() {
        return title;
    }

    @MenuRes
    public Integer getMenuResId() {
        return menuResId;
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public static class Builder {

        private String title = null;
        private Integer menuResId = null;
        private View.OnClickListener onClickListener = null;

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setTitle(@Nullable Context context, @StringRes int titleResId) {
            if (context != null) {
                return setTitle(context.getString(titleResId));
            } else {
                Log.w(TAG, "Failed to set title due to context that is null");
            }
            return this;
        }

        public Builder setMenu(@MenuRes Integer menuResId) {
            this.menuResId = menuResId;
            return this;
        }

        public Builder setOnClickListener(View.OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
            return this;
        }

        public ToolbarProperties build() {
            return new ToolbarProperties(title, menuResId, onClickListener);
        }
    }
}
