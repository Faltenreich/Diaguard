package com.faltenreich.diaguard.ui.list.viewholder;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by Faltenreich on 17.10.2015.
 */
public abstract class BaseViewHolder <T> extends RecyclerView.ViewHolder {

    private Context context;
    private View view;

    private T listItem;

    public BaseViewHolder(View view) {
        super(view);
        this.view = view;
        this.context = view.getContext();
        ButterKnife.bind(this, view);
    }

    protected Context getContext() {
        return context;
    }

    protected View getView() {
        return view;
    }

    T getListItem() {
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