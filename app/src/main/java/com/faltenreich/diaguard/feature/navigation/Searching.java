package com.faltenreich.diaguard.feature.navigation;

import androidx.annotation.Nullable;

public interface Searching {
    SearchProperties getSearchProperties();
    @Nullable SearchOwner getSearchOwner();
}
