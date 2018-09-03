package com.faltenreich.diaguard.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.entity.Tag;
import com.faltenreich.diaguard.ui.view.viewholder.TagViewHolder;

public class TagListAdapter extends BaseAdapter<Tag, TagViewHolder> {

    private TagListener listener;

    public TagListAdapter(Context context) {
        super(context);
    }

    public void setTagListener(TagListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TagViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.list_item_tag, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final TagViewHolder holder, int position) {
        holder.bindData(getItem(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onTagSelected(getItem(holder.getAdapterPosition()), view);
                }
            }
        });
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
        void onTagSelected(Tag tag, View view);
        void onTagDeleted(Tag tag, View view);
    }
}
