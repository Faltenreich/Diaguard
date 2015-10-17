package com.faltenreich.diaguard.ui.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.faltenreich.diaguard.adapter.ListItem;
import com.faltenreich.diaguard.adapter.LogListItem;
import com.faltenreich.diaguard.data.entity.BaseEntity;

import butterknife.ButterKnife;

/**
 * Created by Faltenreich on 17.10.2015.
 */
public abstract class BaseViewHolder <T extends ListItem> extends RecyclerView.ViewHolder {

    private Context context;

    protected BaseViewHolder(View view) {
        super(view);
        this.context = view.getContext();
        ButterKnife.bind(this, view);
    }

    protected Context getContext() {
        return context;
    }

    public abstract void bindData(final T listItem);
}