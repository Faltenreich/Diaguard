package com.faltenreich.diaguard.feature.navigation;

public class ToolbarProperties {

    private final String title;
    private final boolean showToolbar;

    public ToolbarProperties(String title, boolean showToolbar) {
        this.title = title;
        this.showToolbar = showToolbar;
    }

    public String getTitle() {
        return title;
    }

    public boolean showToolbar() {
        return showToolbar;
    }
}
