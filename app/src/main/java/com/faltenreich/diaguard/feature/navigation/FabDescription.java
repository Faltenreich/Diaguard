package com.faltenreich.diaguard.feature.navigation;

import androidx.annotation.Nullable;

public class FabDescription {

    @Nullable private final FabProperties primaryProperties;
    @Nullable private final FabProperties secondaryProperties;
    private final boolean slideOutOnScroll;

    public FabDescription(
        @Nullable FabProperties primaryProperties,
        @Nullable FabProperties secondaryProperties,
        boolean slideOutOnScroll
    ) {
        this.primaryProperties = primaryProperties;
        this.secondaryProperties = secondaryProperties;
        this.slideOutOnScroll = slideOutOnScroll;
    }

    public FabDescription(
        @Nullable FabProperties primaryProperties,
        boolean slideOutOnScroll
    ) {
        this(primaryProperties, null, slideOutOnScroll);
    }

    public FabDescription(
        @Nullable FabProperties primaryProperties
    ) {
        this(primaryProperties, null, true);
    }

    @Nullable
    public FabProperties getPrimaryProperties() {
        return primaryProperties;
    }

    @Nullable
    public FabProperties getSecondaryProperties() {
        return secondaryProperties;
    }

    public boolean slideOutOnScroll() {
        return slideOutOnScroll;
    }
}
