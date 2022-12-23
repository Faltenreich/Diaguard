package com.faltenreich.diaguard.feature.navigation;

import androidx.annotation.Nullable;

public class FabDescription {

    @Nullable private final FabProperties primaryProperties;
    private final boolean slideOutOnScroll;

    public FabDescription(
        @Nullable FabProperties primaryProperties,
        boolean slideOutOnScroll
    ) {
        this.primaryProperties = primaryProperties;
        this.slideOutOnScroll = slideOutOnScroll;
    }

    public FabDescription(
        @Nullable FabProperties primaryProperties
    ) {
        this(primaryProperties, true);
    }

    @Nullable
    public FabProperties getPrimaryProperties() {
        return primaryProperties;
    }

    public boolean slideOutOnScroll() {
        return slideOutOnScroll;
    }
}
