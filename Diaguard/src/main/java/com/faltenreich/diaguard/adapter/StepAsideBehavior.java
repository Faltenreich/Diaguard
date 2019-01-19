package com.faltenreich.diaguard.adapter;

import android.content.Context;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

@SuppressWarnings({"unused", "WeakerAccess"})
public class StepAsideBehavior extends CoordinatorLayout.Behavior<View> {

    private float snackbarTranslation;

    public StepAsideBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout || dependency instanceof RecyclerView;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        if (dependency instanceof Snackbar.SnackbarLayout) {
            translateWithSnackbar(parent, child, (Snackbar.SnackbarLayout) dependency);
            return true;
        }
        return false;
    }

    private void translateWithSnackbar(CoordinatorLayout parent, View child, Snackbar.SnackbarLayout snackbarLayout) {
        float translationY = 0.0F;
        List dependencies = parent.getDependencies(child);
        int i = 0;
        for (int z = dependencies.size(); i < z; ++i) {
            View view = (View) dependencies.get(i);
            if (view instanceof Snackbar.SnackbarLayout && parent.doViewsOverlap(child, view)) {
                translationY = Math.min(translationY, view.getTranslationY() - (float) view.getHeight());
            } else if (view instanceof RecyclerView) {
                RecyclerView recyclerView = (RecyclerView) view;
            }
        }
        if (translationY != snackbarTranslation) {
            ViewCompat.animate(child).cancel();
            if (Math.abs(translationY - snackbarTranslation) == (float) snackbarLayout.getHeight()) {
                ViewCompat.animate(child).translationY(translationY).setListener(null);
            } else {
                child.setTranslationY(translationY);
            }
            snackbarTranslation = translationY;
        }
    }
}