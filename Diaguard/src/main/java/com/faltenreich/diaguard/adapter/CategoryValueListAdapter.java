package com.faltenreich.diaguard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.list.ListItemCategoryValue;
import com.faltenreich.diaguard.ui.view.viewholder.CategoryValueViewHolder;

/**
 * Created by Filip on 04.11.13.
 */
public class CategoryValueListAdapter extends BaseAdapter<ListItemCategoryValue, CategoryValueViewHolder> {

    public CategoryValueListAdapter(Context context) {
        super(context);
    }

    @Override
    public CategoryValueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CategoryValueViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.list_item_table_category_value, parent, false));
    }

    @Override
    public void onBindViewHolder(CategoryValueViewHolder holder, int position) {
        holder.bindData(getItem(position));
    }
}