package com.faltenreich.diaguard.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

@SuppressWarnings({"unused", "WeakerAccess"})
public class SlideOutBehavior extends StepAsideBehavior {

    private boolean slideOut = true;

    public SlideOutBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean isSlideOut() {
        return slideOut;
    }

    public void setSlideOut(boolean slideOut) {
        this.slideOut = slideOut;
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type);
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        if (slideOut) {
            translateWithScrollView(child, dy);
        }
    }

    private void translateWithScrollView(View child, float dy) {
        float margin = child.getLayoutParams() instanceof ViewGroup.MarginLayoutParams ? ((ViewGroup.MarginLayoutParams) child.getLayoutParams()).bottomMargin : 0f;
        child.setTranslationY(Math.max(0f, Math.min(child.getHeight() + margin, child.getTranslationY() + dy)));
    }
}