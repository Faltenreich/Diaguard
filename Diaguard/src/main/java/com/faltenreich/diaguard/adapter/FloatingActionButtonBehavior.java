package com.faltenreich.diaguard.adapter;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;

public class FloatingActionButtonBehavior extends CoordinatorLayout.Behavior {

    public FloatingActionButtonBehavior(Context context, AttributeSet attrs) {
        super();
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL ||
                super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target,
                        nestedScrollAxes);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target,
                               int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed,
                dyUnconsumed);

        FloatingActionButton fab = (FloatingActionButton) child;
        if (dyConsumed > 0
                && child.getVisibility() == View.VISIBLE) {
            fab.hide(true);
        } else if (dyConsumed < 0 && child.getVisibility() != View.VISIBLE) {
            for (int i = 0; i < coordinatorLayout.getChildCount(); i++) {
                if (coordinatorLayout.getChildAt(i) instanceof Snackbar.SnackbarLayout) {
                    fab.show(true);
                    return;
                }
            }

            child.setTranslationY(0.0f);
            fab.show(true);
        }
    }
}