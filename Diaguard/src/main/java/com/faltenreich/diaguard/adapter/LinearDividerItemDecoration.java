package com.faltenreich.diaguard.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.faltenreich.diaguard.R;

/**
 * Created by Faltenreich on 17.12.2015.
 */
public class LinearDividerItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable drawable;

    public LinearDividerItemDecoration(Context context) {
        drawable = ContextCompat.getDrawable(context, R.drawable.line_divider);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int index = 0; index < childCount; index++) {
            View child = parent.getChildAt(index);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + drawable.getIntrinsicHeight();

            drawable.setBounds(left, top, right, bottom);
            drawable.draw(c);
        }
    }
}