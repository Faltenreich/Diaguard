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

import java.util.Collections;

import androidx.annotation.NonNull;

public class CategoryListAdapter extends BaseAdapter<Measurement.Category, CategoryViewHolder> implements DragDropItemTouchHelperCallback.DragDropListener {

    private OrderListener listener;

    public CategoryListAdapter(Context context, OrderListener listener) {
        super(context);
        this.listener = listener;
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

    @Override
    public void onItemMoved(int oldPosition, int newPosition) {
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
        listener.onOrderChanges();
    }

    public interface OrderListener {
        void onOrderChanges();
    }
}
