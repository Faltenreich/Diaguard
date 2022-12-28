package com.faltenreich.diaguard.shared.view.recyclerview.decoration;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * ListDividerItemDecoration is a {@link RecyclerView.ItemDecoration} that can be used as a divider
 * between items of a {@link LinearLayoutManager}. It supports both {@link #HORIZONTAL} and
 * {@link #VERTICAL} orientations and also includes the fix of {@link DividerItemDecorationFix}
 * to remove the divider after the last item.
 */
public class ListDividerItemDecoration extends DividerItemDecorationFix {

    public ListDividerItemDecoration(Context context, int orientation) {
        super(context, orientation);
    }

    public ListDividerItemDecoration(Context context) {
        this(context, RecyclerView.VERTICAL);
    }
}