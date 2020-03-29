package com.faltenreich.diaguard.feature.timeline.day;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.faltenreich.diaguard.R;

class TimelineDayItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable divider;

    TimelineDayItemDecoration(Context context) {
        divider = ContextCompat.getDrawable(context, R.drawable.separator);
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