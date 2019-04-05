package com.faltenreich.diaguard.ui.list.adapter;

import android.content.Context;
import android.os.Handler;
import androidx.annotation.CallSuper;

import com.faltenreich.diaguard.ui.list.item.ListItemDate;
import com.faltenreich.diaguard.ui.list.viewholder.BaseViewHolder;

/**
 * Created by Filip on 04.11.13.
 */
public abstract class EndlessAdapter<L extends ListItemDate, VH extends BaseViewHolder<L>> extends BaseAdapter<L, VH> {

    private static final int VISIBLE_THRESHOLD = 5;
    static final int BULK_SIZE = VISIBLE_THRESHOLD * 2;

    private OnEndlessListener listener;

    EndlessAdapter(Context context) {
        super(context);
    }

    @CallSuper
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

    public void setOnEndlessListener(OnEndlessListener listener) {
        this.listener = listener;
    }

    public interface OnEndlessListener {
        void onLoadMore(boolean scrollingDown);
    }
}