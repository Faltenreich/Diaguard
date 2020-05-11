package com.faltenreich.diaguard.feature.category;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.view.recyclerview.adapter.BaseAdapter;
import com.faltenreich.diaguard.shared.view.recyclerview.drag.DragDropItemTouchHelperCallback;

import java.util.Collections;

class CategoryListAdapter extends BaseAdapter<Category, CategoryViewHolder> implements DragDropItemTouchHelperCallback.DragDropListener {

    private Listener listener;

    CategoryListAdapter(Context context, Listener listener) {
        super(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(parent, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryViewHolder holder, int position) {
        holder.bindData(getItem(position));
    }

    @Override
    public void onItemDragEnd(int oldPosition, int newPosition) {
        if (oldPosition < newPosition) {
            for (int position = oldPosition; position < newPosition; position++) {
                Collections.swap(getItems(), position, position + 1);
            }
        } else {
            for (int position = oldPosition; position > newPosition; position--) {
                Collections.swap(getItems(), position, position - 1);
            }
        }
        notifyItemMoved(oldPosition, newPosition);
        listener.onReorderEnd();
    }

    interface Listener {
        void onReorderStart(RecyclerView.ViewHolder viewHolder);
        void onReorderEnd();
        void onCheckedChange();
    }
}
