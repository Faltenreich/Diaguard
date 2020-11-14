package com.faltenreich.diaguard.feature.navigation;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public interface Navigating {

    void openFragment(
        @NonNull Fragment fragment,
        Navigator.Operation operation,
        boolean addToBackStack
    );
}
