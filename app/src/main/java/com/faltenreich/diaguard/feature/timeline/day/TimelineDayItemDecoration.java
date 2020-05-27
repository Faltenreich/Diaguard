package com.faltenreich.diaguard.feature.timeline.day;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faltenreich.diaguard.shared.view.resource.DrawableUtils;

class TimelineDayItemDecoration extends DividerItemDecoration {

    private final Drawable divider;
    private final Rect bounds;

    TimelineDayItemDecoration(Context context) {
        super(context, RecyclerView.VERTICAL);
        divider = DrawableUtils.getListDivider(context);
        bounds = new Rect();
    }

    @Override
    public void onDraw(@NonNull Canvas canvas, RecyclerView parent, @NonNull RecyclerView.State state) {
        if (parent.getLayoutManager() == null || !(parent.getLayoutManager() instanceof GridLayoutManager)) {
            return;
        }
        // Workaround: We re-implement drawing methods as they are private in super class
        int spanCount = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
        canvas.save();
        drawHorizontalLines(canvas, parent, spanCount);
        drawVerticalLines(canvas, parent, spanCount);
        canvas.restore();
    }

    private void drawHorizontalLines(Canvas canvas, RecyclerView parent, int spanCount) {
        int left;
        int right;

        if (parent.getClipToPadding()) {
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();
            canvas.clipRect(left, parent.getPaddingTop(), right,
                parent.getHeight() - parent.getPaddingBottom());
        } else {
            left = 0;
            right = parent.getWidth();
        }

        for (int row = 0; row < parent.getChildCount() / spanCount; row++) {
            View child = parent.getChildAt(row * spanCount);
            parent.getDecoratedBoundsWithMargins(child, bounds);
            int bottom = bounds.bottom + Math.round(child.getTranslationY());
            int top = bottom - divider.getIntrinsicHeight();
            divider.setBounds(left, top, right, bottom);
            divider.draw(canvas);
        }
    }

    private void drawVerticalLines(Canvas canvas, RecyclerView parent, int spanCount) {
        int top;
        int bottom;

        if (parent.getClipToPadding()) {
            top = parent.getPaddingTop();
            bottom = parent.getHeight() - parent.getPaddingBottom();
            canvas.clipRect(parent.getPaddingLeft(), top,
                parent.getWidth() - parent.getPaddingRight(), bottom);
        } else {
            top = 0;
            bottom = parent.getHeight();
        }

        for (int column = 0; column < spanCount; column++) {
            View child = parent.getChildAt(column);
            parent.getDecoratedBoundsWithMargins(child, bounds);
            int right = bounds.right + Math.round(child.getTranslationX());
            int left = right - divider.getIntrinsicWidth();
            divider.setBounds(left, top, right, bottom);
            divider.draw(canvas);
        }
    }
}