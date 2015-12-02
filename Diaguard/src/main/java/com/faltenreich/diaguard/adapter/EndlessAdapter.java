package com.faltenreich.diaguard.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.faltenreich.diaguard.ui.viewholder.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Filip on 04.11.13.
 */
public abstract class EndlessAdapter<L extends ListItem, VH extends BaseViewHolder<L>> extends BaseAdapter<L, VH> {

    public enum Direction {
        UP,
        DOWN
    }

    private static final int THRESHOLD = 5;

    private OnEndlessListener listener;

    public EndlessAdapter(Context context) {
        super(context);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        if (listener != null) {
            if (position < THRESHOLD) {
                listener.onLoadMore(Direction.UP);
            } else if (position > getItemCount() - THRESHOLD) {
                listener.onLoadMore(Direction.DOWN);
            }
        }
    }

    public void setOnEndlessListener(OnEndlessListener listener) {
        this.listener = listener;
    }

    public interface OnEndlessListener {
        void onLoadMore(Direction direction);
    }
}