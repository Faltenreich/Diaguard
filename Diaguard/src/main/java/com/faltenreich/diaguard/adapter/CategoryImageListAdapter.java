package com.faltenreich.diaguard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.list.ListItemCategoryImage;
import com.faltenreich.diaguard.ui.view.viewholder.CategoryImageViewHolder;

/**
 * Created by Filip on 04.11.13.
 */
public class CategoryImageListAdapter extends BaseAdapter<ListItemCategoryImage, CategoryImageViewHolder> {

    public CategoryImageListAdapter(Context context) {
        super(context);
    }

    @Override
    public CategoryImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CategoryImageViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.list_item_table_category_image, parent, false));
    }

    @Override
    public void onBindViewHolder(CategoryImageViewHolder holder, int position) {
        holder.bindData(getItem(position));
    }
}