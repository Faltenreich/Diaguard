package com.faltenreich.diaguard.ui.recycler;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import org.joda.time.DateTime;

import java.util.Date;

/**
 * Created by Faltenreich on 03.08.2015.
 */
public class LogRecyclerView extends RecyclerView {

    LogRecyclerViewCallback callback;

    public LogRecyclerView(Context context) {
        super(context);
    }

    public LogRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /*
    public void setup(LogRecyclerViewCallback callback) {
        this.callback = callback;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        setLayoutManager(linearLayoutManager);

        DateTime dateTime = DateTime.now();
        LogRecyclerAdapter recyclerAdapter = new LogRecyclerAdapter(getContext(), dateTime);
        setAdapter(recyclerAdapter);
        scrollToPosition(recyclerAdapter.getPosition(dateTime));

        // Endless scroll
        addOnScrollListener(new EndlessScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(Direction direction) {
                recyclerAdapter.appendRows(direction);
            }
        });

        // Fragment updates
        addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                RecyclerItem item = recyclerAdapter.items.get(linearLayoutManager.findFirstVisibleItemPosition());
                DateTime dateTime = item.getDay();

                // Update month in Toolbar when section is being crossed
                boolean isScrollingUp = dy < 0;
                boolean isCrossingMonth = isScrollingUp ?
                        dateTime.dayOfMonth().get() == dateTime.dayOfMonth().getMinimumValue() :
                        dateTime.dayOfMonth().get() == dateTime.dayOfMonth().getMaximumValue();

                if (isCrossingDay) {
                    updateMonthForUi();
                }
            }
        });
    }
    */

    public interface LogRecyclerViewCallback {
        void onScrolledRow(DateTime day);
    }
}
