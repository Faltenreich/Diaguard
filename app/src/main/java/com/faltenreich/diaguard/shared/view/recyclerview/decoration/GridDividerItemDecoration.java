package com.faltenreich.diaguard.shared.view.recyclerview.decoration;

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

public class GridDividerItemDecoration extends DividerItemDecoration {

    private final Drawable divider;
    private final Rect bounds;

    public GridDividerItemDecoration(Context context) {
        super(context, RecyclerView.VERTICAL);
        divider = DrawableUtils.getDivider(context);
        bounds = new Rect();
    }

    @Override
    public void onDraw(@NonNull Canvas canvas, RecyclerView parent, @NonNull RecyclerView.State state) {
        if (parent.getLayoutManager() == null || !(parent.getLayoutManager() instanceof GridLayoutManager)) {
            return;
        }
        canvas.save();

        int spanCount = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
        drawHorizontalLines(canvas, parent, spanCount);
        drawVerticalLines(canvas, parent, spanCount);

        canvas.restore();
    }

    private void drawHorizontalLines(Canvas canvas, RecyclerView parent, int spanCount) {
        for (int row = 0; row < (parent.getChildCount() / spanCount) - 1; row++) {
            View child = parent.getChildAt(row * spanCount);
            if (child != null) {
                parent.getDecoratedBoundsWithMargins(child, bounds);
                int left = 0;
                int right = parent.getWidth();
                int bottom = bounds.bottom + Math.round(child.getTranslationY());
                int top = bottom - divider.getIntrinsicHeight();
                divider.setBounds(left, top, right, bottom);
                divider.draw(canvas);
            }
        }
    }

    private void drawVerticalLines(Canvas canvas, RecyclerView parent, int spanCount) {
        for (int column = 0; column < spanCount; column++) {
            View child = parent.getChildAt(column);
            if (child != null) {
                parent.getDecoratedBoundsWithMargins(child, bounds);
                int left = bounds.left;
                int right = left + divider.getIntrinsicWidth() + Math.round(child.getTranslationX());
                int top = 0;
                int bottom = parent.getHeight();
                divider.setBounds(left, top, right, bottom);
                divider.draw(canvas);
            }
        }
    }
}