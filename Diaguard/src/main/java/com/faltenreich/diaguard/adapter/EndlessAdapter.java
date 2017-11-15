package com.faltenreich.diaguard.adapter;

import android.content.Context;
import android.os.Handler;

import com.faltenreich.diaguard.adapter.list.ListItemDate;
import com.faltenreich.diaguard.ui.view.viewholder.BaseViewHolder;

/**
 * Created by Filip on 04.11.13.
 */
public abstract class EndlessAdapter<L extends ListItemDate, VH extends BaseViewHolder<L>> extends BaseAdapter<L, VH> {

    static final int VISIBLE_THRESHOLD = 5;

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
                            listener.onLoadMore(false);
                        } else if (holder.getAdapterPosition() > getItemCount() - VISIBLE_THRESHOLD) {
                            listener.onLoadMore(true);
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

    interface OnEndlessListener {
        void onLoadMore(boolean scrollingDown);
    }
}