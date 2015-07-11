package com.faltenreich.diaguard.ui.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Filip on 12.07.2015.
 */
public class Behavior extends CoordinatorLayout.Behavior {

    public Behavior(Context context, AttributeSet attrs) {}

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View view, View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View view, View dependency) {
        float translationY = Math.min(0, dependency.getTranslationY() - dependency.getHeight());
        view.setTranslationY(translationY);
        return true;
    }
}