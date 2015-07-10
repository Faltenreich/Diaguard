package com.faltenreich.diaguard.adapters.recycler;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Filip on 05.07.2015.
 */
public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener {

    public enum Direction {
        UP,
        DOWN
    }

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

        Direction direction = getDirection(dy);
        if (!loading && hasToLoadMore(direction)) {
            onLoadMore(direction);
            loading = true;
        }
    }

    private boolean hasFinishedLoading() {
        return totalItemCount > previousTotal;
    }

    private boolean hasToLoadMore(Direction direction) {
        switch (direction) {
            case DOWN:
                return (totalItemCount - visibleItemCount) <= (firstVisibleItem + VISIBLE_THRESHOLD);
            case UP:
                return firstVisibleItem <= VISIBLE_THRESHOLD;
            default:
                return false;
        }
    }

    private Direction getDirection(int dy) {
        return dy > 0 ? Direction.DOWN : Direction.UP;
    }

    public abstract void onLoadMore(Direction direction);
}