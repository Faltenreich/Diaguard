package com.faltenreich.diaguard.shared.view.recyclerview.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.ButterKnife;

/**
 * Created by Faltenreich on 17.10.2015.
 */
public abstract class BaseViewHolder <T> extends RecyclerView.ViewHolder {

    private Context context;
    private T item;

    public BaseViewHolder(ViewGroup parent, @LayoutRes int layoutResource) {
        super(LayoutInflater.from(parent.getContext()).inflate(layoutResource, parent, false));
        this.context = parent.getContext();
        ButterKnife.bind(this, itemView);
    }

    protected Context getContext() {
        return context;
    }

    protected T getItem() {
        return item;
    }

    public void bind(T item) {
        this.item = item;
        onBind(item);
    }

    protected abstract void onBind(T item);

    public int getSwipeFlags() {
        return 0;
    }
}