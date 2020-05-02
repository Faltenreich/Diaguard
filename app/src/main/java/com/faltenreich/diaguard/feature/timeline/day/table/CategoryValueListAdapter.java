package com.faltenreich.diaguard.feature.timeline.day.table;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.shared.view.recyclerview.adapter.BaseAdapter;

/**
 * Created by Filip on 04.11.13.
 */
public class CategoryValueListAdapter extends BaseAdapter<CategoryValueListItem, CategoryValueViewHolder> {

    public CategoryValueListAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public CategoryValueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryValueViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.list_item_table_category_value, parent, false));
    }

    @Override
    public void onBindViewHolder(CategoryValueViewHolder holder, int position) {
        holder.bindData(getItem(position));
    }
}