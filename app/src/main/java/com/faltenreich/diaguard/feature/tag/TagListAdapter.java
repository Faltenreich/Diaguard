package com.faltenreich.diaguard.feature.tag;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.faltenreich.diaguard.shared.data.database.entity.Tag;
import com.faltenreich.diaguard.shared.view.recyclerview.adapter.BaseAdapter;

class TagListAdapter extends BaseAdapter<Tag, TagViewHolder> {

    private final TagListener listener;

    TagListAdapter(Context context, TagListener listener) {
        super(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TagViewHolder(parent, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull final TagViewHolder holder, int position) {
        holder.bind(getItem(position));
    }
}
