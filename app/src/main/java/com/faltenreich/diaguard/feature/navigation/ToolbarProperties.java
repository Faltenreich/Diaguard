package com.faltenreich.diaguard.feature.navigation;

import android.content.Context;

import androidx.annotation.MenuRes;
import androidx.annotation.StringRes;

public class ToolbarProperties {

    private final String title;
    private final int menuResId;
    private final boolean showToolbar;

    private ToolbarProperties(String title, @MenuRes int menuResId, boolean showToolbar) {
        this.title = title;
        this.menuResId = menuResId;
        this.showToolbar = showToolbar;
    }

    public String getTitle() {
        return title;
    }

    @MenuRes
    public int getMenuResId() {
        return menuResId;
    }

    public boolean showToolbar() {
        return showToolbar;
    }

    public static class Builder {

        private String title = null;
        private int menuResId = -1;
        private boolean showToolbar = true;

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setTitle(Context context, @StringRes int titleResId) {
            return setTitle(context.getString(titleResId));
        }

        public Builder setMenu(@MenuRes int menuResId) {
            this.menuResId = menuResId;
            return this;
        }

        public Builder setShowToolbar(boolean showToolbar) {
            this.showToolbar = showToolbar;
            return this;
        }

        public ToolbarProperties build() {
            return new ToolbarProperties(title, menuResId, showToolbar);
        }
    }
}
