package com.faltenreich.diaguard.feature.navigation;

public interface ToolbarProperties {
    String getTitle();
    default boolean showToolbar() { return true; }
}
