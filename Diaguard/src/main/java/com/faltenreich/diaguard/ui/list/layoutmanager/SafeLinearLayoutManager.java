package com.faltenreich.diaguard.ui.list.layoutmanager;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;

public class SafeLinearLayoutManager extends LinearLayoutManager {

    private static final String TAG = SafeLinearLayoutManager.class.getSimpleName();

    public SafeLinearLayoutManager(Context context) {
        super(context);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
