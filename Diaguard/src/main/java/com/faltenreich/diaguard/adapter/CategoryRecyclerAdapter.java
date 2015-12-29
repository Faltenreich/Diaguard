package com.faltenreich.diaguard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.list.ListItemCategoryValues;
import com.faltenreich.diaguard.ui.view.viewholder.TableCategoryHolder;

/**
 * Created by Filip on 04.11.13.
 */
public class CategoryRecyclerAdapter extends BaseAdapter<ListItemCategoryValues, TableCategoryHolder> {

    public CategoryRecyclerAdapter(Context context) {
        super(context);
    }

    @Override
    public TableCategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TableCategoryHolder(LayoutInflater.from(getContext()).inflate(R.layout.list_item_table, parent, false));
    }

    @Override
    public void onBindViewHolder(TableCategoryHolder holder, int position) {
        holder.bindData(getItem(position));
    }

    @Override
    public void onViewRecycled(TableCategoryHolder holder) {
        super.onViewRecycled(holder);
        holder.content.removeAllViews();
    }
}