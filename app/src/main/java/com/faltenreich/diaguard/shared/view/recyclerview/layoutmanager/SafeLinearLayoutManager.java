package com.faltenreich.diaguard.shared.view.recyclerview.layoutmanager;

import android.content.Context;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
