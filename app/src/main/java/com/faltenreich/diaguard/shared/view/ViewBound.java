package com.faltenreich.diaguard.shared.view;

import androidx.viewbinding.ViewBinding;

public interface ViewBound<BINDING extends ViewBinding> {
    BINDING getBinding();
}
