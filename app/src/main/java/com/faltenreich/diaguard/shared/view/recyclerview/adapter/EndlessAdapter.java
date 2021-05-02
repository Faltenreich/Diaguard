package com.faltenreich.diaguard.shared.view.recyclerview.adapter;

import android.content.Context;
import android.os.Handler;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;

import com.faltenreich.diaguard.feature.log.LogListItem;
import com.faltenreich.diaguard.shared.view.recyclerview.viewholder.BaseViewHolder;

/**
 * Created by Filip on 04.11.13.
 */
public abstract class EndlessAdapter<L extends LogListItem, VH extends BaseViewHolder<?, L>> extends BaseAdapter<L, VH> {

    private static final int VISIBLE_THRESHOLD = 5;
    public static final int BULK_SIZE = VISIBLE_THRESHOLD * 2;

    private OnEndlessListener listener;

    public EndlessAdapter(Context context) {
        super(context);
    }

    @CallSuper
    @Override
    public void onBindViewHolder(@NonNull final VH holder, int position) {
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