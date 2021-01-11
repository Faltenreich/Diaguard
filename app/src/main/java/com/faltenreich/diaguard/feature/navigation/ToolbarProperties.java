package com.faltenreich.diaguard.feature.navigation;

import android.content.Context;
import android.util.Log;

import androidx.annotation.MenuRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

public class ToolbarProperties {

    private static final String TAG = ToolbarProperties.class.getSimpleName();

    private final String title;
    private final Integer menuResId;

    private ToolbarProperties(
        String title,
        @MenuRes Integer menuResId
    ) {
        this.title = title;
        this.menuResId = menuResId;
    }

    public String getTitle() {
        return title;
    }

    @MenuRes
    public Integer getMenuResId() {
        return menuResId;
    }

    public static class Builder {

        private String title = null;
        private Integer menuResId = null;

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

        public ToolbarProperties build() {
            return new ToolbarProperties(title, menuResId);
        }
    }
}
