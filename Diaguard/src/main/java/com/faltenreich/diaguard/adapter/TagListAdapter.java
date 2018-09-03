package com.faltenreich.diaguard.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.entity.Tag;
import com.faltenreich.diaguard.ui.view.viewholder.TagViewHolder;

public class TagListAdapter extends BaseAdapter<Tag, TagViewHolder> {

    public TagListAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TagViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.list_item_tag, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TagViewHolder holder, int position) {
        holder.bindData(getItem(position));
    }
}
