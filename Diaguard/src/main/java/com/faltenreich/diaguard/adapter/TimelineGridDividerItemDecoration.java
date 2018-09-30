package com.faltenreich.diaguard.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.faltenreich.diaguard.R;

public class TimelineGridDividerItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable divider;

    public TimelineGridDividerItemDecoration(Context context) {
        divider = ContextCompat.getDrawable(context, R.drawable.line_divider);
    }

    @Override
    public void onDrawOver(@NonNull Canvas canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int top = parent.getPaddingTop();
        int right = parent.getWidth() - parent.getPaddingRight();
        int bottom = parent.getHeight() - parent.getPaddingBottom();

        int childCount = parent.getChildCount();
        for (int index = 0; index < childCount; index++) {
            View child = parent.getChildAt(index);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int childTop = child.getBottom() + params.bottomMargin;
            int childBottom = childTop + divider.getIntrinsicHeight();

            divider.setBounds(left, childTop, right, childBottom);
            divider.draw(canvas);

            int childLeft = child.getLeft() + params.leftMargin;
            int childRight = childLeft + divider.getIntrinsicWidth();

            divider.setBounds(childLeft, top, childRight, bottom);
            divider.draw(canvas);
        }
    }
}