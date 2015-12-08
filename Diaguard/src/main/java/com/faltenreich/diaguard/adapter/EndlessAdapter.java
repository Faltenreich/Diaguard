package com.faltenreich.diaguard.adapter;

import android.content.Context;
import android.os.Handler;

import com.faltenreich.diaguard.adapter.list.ListItem;
import com.faltenreich.diaguard.ui.view.viewholder.BaseViewHolder;

/**
 * Created by Filip on 04.11.13.
 */
public abstract class EndlessAdapter<L extends ListItem, VH extends BaseViewHolder<L>> extends BaseAdapter<L, VH> {

    public static final int VISIBLE_THRESHOLD = 5;
    public static final int BULK_SIZE = 10;

    private OnEndlessListener listener;

    public EndlessAdapter(Context context) {
        super(context);
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        if (listener != null) {
            // Handler is required to make layout changes during scroll
            Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                public void run() {
                    if (listener != null) {
                        if (position < VISIBLE_THRESHOLD) {
                            listener.onLoadMore(Direction.UP);
                        } else if (position > getItemCount() - VISIBLE_THRESHOLD) {
                            listener.onLoadMore(Direction.DOWN);
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

    public void removeOnEndlessListener() {
        this.listener = null;
    }

    public enum Direction {
        UP,
        DOWN
    }

    public interface OnEndlessListener {
        void onLoadMore(Direction direction);
    }
}