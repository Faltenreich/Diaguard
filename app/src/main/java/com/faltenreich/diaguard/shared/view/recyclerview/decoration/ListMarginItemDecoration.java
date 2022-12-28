package com.faltenreich.diaguard.shared.view.recyclerview.decoration;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListMarginItemDecoration extends RecyclerView.ItemDecoration {

    private final int margin;

    public ListMarginItemDecoration(Context context, @DimenRes int marginResId) {
        this.margin = context.getResources().getDimensionPixelSize(marginResId);
    }

    @Override
    public void getItemOffsets(
        @NonNull Rect outRect,
        @NonNull View view,
        @NonNull RecyclerView parent,
        @NonNull RecyclerView.State state
    ) {
        outRect.set(
            0,
            parent.getChildAdapterPosition(view) == 0 ? 0 : margin,
            0,
            0
        );
    }
}
