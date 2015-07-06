package com.faltenreich.diaguard.adapters;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Filip on 05.07.2015.
 */
public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener {

    // The minimum amount of items to have below your current scroll position before loading more
    private static final int VISIBLE_THRESHOLD = 5;

    // The total number of items in the dataset after the last load
    private int previousTotal = 0;

    // True if we are still waiting for the last set of data to load
    private boolean loading = true;

    private int firstVisibleItem;
    private int visibleItemCount;
    private int totalItemCount;

    private LinearLayoutManager layoutManager;

    public EndlessScrollListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = layoutManager.getItemCount();
        firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

        if (loading && hasFinishedLoading()) {
            previousTotal = totalItemCount;
            loading = false;
        }

        if (!loading && hasToLoadMore(dy)) {
            onLoadMore(isScrollingDown(dy));
            loading = true;
        }
    }

    private boolean hasFinishedLoading() {
        return totalItemCount > previousTotal;
    }

    private boolean hasToLoadMore(int dy) {
        return isScrollingDown(dy) ?
                (totalItemCount - visibleItemCount) <= (firstVisibleItem + VISIBLE_THRESHOLD) :
                firstVisibleItem <= VISIBLE_THRESHOLD;
    }

    private boolean isScrollingDown(int dy) {
        return dy > 0;
    }

    public abstract void onLoadMore(boolean isScrollingDown);
}