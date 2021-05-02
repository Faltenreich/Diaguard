package com.faltenreich.diaguard.feature.navigation;

import androidx.annotation.StringRes;

public class TabProperties {

    private final int titleResId;

    private TabProperties(@StringRes int titleResId) {
        this.titleResId = titleResId;
    }

    @StringRes
    public int getTitleResId() {
        return titleResId;
    }

    public static class Builder {

        private final int titleResId;

        public Builder(@StringRes int titleResId) {
            this.titleResId = titleResId;
        }

        public TabProperties build() {
            return new TabProperties(titleResId);
        }
    }
}
