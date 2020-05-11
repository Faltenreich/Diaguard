package com.faltenreich.diaguard.feature.tag;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.faltenreich.diaguard.shared.data.database.entity.Tag;
import com.faltenreich.diaguard.shared.view.recyclerview.adapter.BaseAdapter;

class TagListAdapter extends BaseAdapter<Tag, TagViewHolder> {

    private TagListener listener;

    TagListAdapter(Context context) {
        super(context);
    }

    void setTagListener(TagListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TagViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull final TagViewHolder holder, int position) {
        holder.bindData(getItem(position));
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onTagDeleted(getItem(holder.getAdapterPosition()), view);
                }
            }
        });
    }

    public interface TagListener {
        void onTagDeleted(Tag tag, View view);
    }
}
