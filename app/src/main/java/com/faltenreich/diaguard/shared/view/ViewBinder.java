package com.faltenreich.diaguard.shared.view;

public class ViewBinder<BINDING extends androidx.viewbinding.ViewBinding> {

    private final BINDING binding;

    public ViewBinder(BINDING binding) {
        this.binding = binding;
    }

    public BINDING getBinding() {
        return binding;
    }
}
