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
    private T listItem;

    public BaseViewHolder(ViewGroup parent, @LayoutRes int layoutResource) {
        super(LayoutInflater.from(parent.getContext()).inflate(layoutResource, parent, false));
        this.context = parent.getContext();
        ButterKnife.bind(this, itemView);
    }

    protected Context getContext() {
        return context;
    }

    protected T getListItem() {
        return listItem;
    }

    public void bindData(T listItem) {
        this.listItem = listItem;
        bindData();
    }

    protected abstract void bindData();

    public void setListItem(T listItem) {
        this.listItem = listItem;
    }

    public int getSwipeFlags() {
        return 0;
    }
}