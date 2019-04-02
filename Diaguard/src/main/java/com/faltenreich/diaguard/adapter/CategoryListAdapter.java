package com.faltenreich.diaguard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.data.entity.Tag;
import com.faltenreich.diaguard.ui.view.viewholder.CategoryViewHolder;
import com.faltenreich.diaguard.ui.view.viewholder.TagViewHolder;

import androidx.annotation.NonNull;

public class CategoryListAdapter extends BaseAdapter<Measurement.Category, CategoryViewHolder> {

    public CategoryListAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.list_item_category, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryViewHolder holder, int position) {
        holder.bindData(getItem(position));
    }
}
