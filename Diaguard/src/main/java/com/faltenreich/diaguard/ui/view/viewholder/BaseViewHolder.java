package com.faltenreich.diaguard.ui.view.viewholder;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.faltenreich.diaguard.adapter.list.ListItem;
import com.faltenreich.diaguard.adapter.list.ListItemDate;

import butterknife.ButterKnife;

/**
 * Created by Faltenreich on 17.10.2015.
 */
public abstract class BaseViewHolder <T extends ListItem> extends RecyclerView.ViewHolder {

    private Context context;
    private View view;

    private T listItem;

    protected BaseViewHolder(View view) {
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

    protected T getListItem() {
        return listItem;
    }

    public void bindData(T listItem) {
        this.listItem = listItem;
        bindData();
    }

    public void setBackgroundColor(@ColorRes int colorResourceId) {
        getView().setBackgroundColor(ContextCompat.getColor(getContext(), colorResourceId));
    }

    protected abstract void bindData();
}