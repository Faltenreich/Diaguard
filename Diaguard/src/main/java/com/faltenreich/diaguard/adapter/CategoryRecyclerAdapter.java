package com.faltenreich.diaguard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.list.ListItemCategory;
import com.faltenreich.diaguard.adapter.list.ListItemCategoryValue;
import com.faltenreich.diaguard.ui.view.viewholder.BaseViewHolder;
import com.faltenreich.diaguard.ui.view.viewholder.CategoryImageViewHolder;
import com.faltenreich.diaguard.ui.view.viewholder.CategoryValueViewHolder;

/**
 * Created by Filip on 04.11.13.
 */
public class CategoryRecyclerAdapter extends BaseAdapter<ListItemCategory, BaseViewHolder<ListItemCategory>> {

    public static final int VIEW_TYPE_IMAGE = 0;
    public static final int VIEW_TYPE_VALUE = 1;

    public CategoryRecyclerAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position) instanceof ListItemCategoryValue ? VIEW_TYPE_VALUE : VIEW_TYPE_IMAGE;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return viewType == VIEW_TYPE_IMAGE ?
                new CategoryImageViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.list_item_table_category_image, parent, false)):
                new CategoryValueViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.list_item_table_category_value, parent, false));
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.bindData(getItem(position));
    }
}