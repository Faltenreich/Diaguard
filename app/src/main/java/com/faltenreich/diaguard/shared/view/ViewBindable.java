package com.faltenreich.diaguard.shared.view;

import androidx.viewbinding.ViewBinding;

public interface ViewBindable<BINDING extends ViewBinding> {
    BINDING getBinding();
}
