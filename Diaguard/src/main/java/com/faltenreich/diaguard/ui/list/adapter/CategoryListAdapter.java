package com.faltenreich.diaguard.ui.list.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.ui.list.helper.DragDropItemTouchHelperCallback;
import com.faltenreich.diaguard.ui.list.viewholder.CategoryViewHolder;

import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryListAdapter extends BaseAdapter<Measurement.Category, CategoryViewHolder> implements DragDropItemTouchHelperCallback.DragDropListener {

    private ReorderListener listener;

    public CategoryListAdapter(Context context, ReorderListener listener) {
        super(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.list_item_category, parent, false), listener);
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

    public interface ReorderListener {
        void onReorderStart(RecyclerView.ViewHolder viewHolder);
        void onReorderEnd();
    }
}
