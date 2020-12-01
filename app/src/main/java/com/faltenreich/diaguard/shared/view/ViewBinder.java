package com.faltenreich.diaguard.shared.view;

import androidx.viewbinding.ViewBinding;

public class ViewBinder<BINDING extends ViewBinding> {

    private final BINDING binding;

    public ViewBinder(BINDING binding) {
        this.binding = binding;
    }

    public BINDING getBinding() {
        return binding;
    }
}
