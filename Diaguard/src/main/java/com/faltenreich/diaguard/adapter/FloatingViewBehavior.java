package com.faltenreich.diaguard.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;

import java.util.List;

@SuppressWarnings("unused")
public class FloatingViewBehavior extends CoordinatorLayout.Behavior<View> {

    private float snackbarTranslation;

    public FloatingViewBehavior(Context context, AttributeSet attrs) {
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

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type);
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
        if (child instanceof FloatingActionButton) {
            translateWithScrollView(coordinatorLayout, child, dyConsumed);
        }
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
                Log.d("Test", "Scroll offset: " + recyclerView.getScrollY());
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

    private void translateWithScrollView(CoordinatorLayout parent, View child, float dyConsumed) {
        FloatingActionButton fab = (FloatingActionButton) child;
        if (dyConsumed > 0 && child.getVisibility() == View.VISIBLE) {
            fab.hide(true);
        } else if (dyConsumed < 0 && child.getVisibility() != View.VISIBLE) {
            for (int i = 0; i < parent.getChildCount(); i++) {
                if (parent.getChildAt(i) instanceof Snackbar.SnackbarLayout) {
                    fab.show(true);
                    return;
                }
            }
            child.setTranslationY(0.0f);
            fab.show(true);
        }
    }
}