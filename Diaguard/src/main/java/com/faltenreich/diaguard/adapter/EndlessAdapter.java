package com.faltenreich.diaguard.adapter;

import android.content.Context;
import android.os.Handler;

import com.faltenreich.diaguard.adapter.list.ListItemDate;
import com.faltenreich.diaguard.ui.view.viewholder.BaseViewHolder;

/**
 * Created by Filip on 04.11.13.
 */
abstract class EndlessAdapter<L extends ListItemDate, VH extends BaseViewHolder<L>> extends BaseAdapter<L, VH> {

    private static final int VISIBLE_THRESHOLD = 5;
    static final int BULK_SIZE = 10;

    private OnEndlessListener listener;

    EndlessAdapter(Context context) {
        super(context);
    }

    @Override
    public void onBindViewHolder(final VH holder, int position) {
        if (listener != null) {
            // Handler is required to make layout changes during scroll
            Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                public void run() {
                    if (listener != null) {
                        if (holder.getAdapterPosition() < VISIBLE_THRESHOLD) {
                            listener.onLoadMore(Direction.UP);
                        } else if (holder.getAdapterPosition() > getItemCount() - VISIBLE_THRESHOLD) {
                            listener.onLoadMore(Direction.DOWN);
                        }
                    }
                }
            };
            handler.post(runnable);
        }
    }

    void setOnEndlessListener(OnEndlessListener listener) {
        this.listener = listener;
    }

    void removeOnEndlessListener() {
        this.listener = null;
    }

    enum Direction {
        UP,
        DOWN
    }

    interface OnEndlessListener {
        void onLoadMore(Direction direction);
    }
}