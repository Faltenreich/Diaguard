package com.faltenreich.diaguard.shared.view.recyclerview.decoration;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

public class VerticalDividerItemDecoration extends DividerItemDecorationFix {

    public VerticalDividerItemDecoration(Context context) {
        super(context, RecyclerView.VERTICAL);
    }
}