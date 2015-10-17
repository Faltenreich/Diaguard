package com.faltenreich.diaguard.adapter;

import android.support.v7.widget.RecyclerView;

import com.faltenreich.diaguard.data.entity.BaseEntity;
import com.faltenreich.diaguard.ui.viewholder.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Filip on 04.11.13.
 */
public abstract class BaseAdapter<L extends ListItem, VH extends BaseViewHolder<L>> extends RecyclerView.Adapter<VH> {

    public List<L> items;

    public BaseAdapter() {
        this.items = new ArrayList<>();
    }

    public void addItem(L item) {
        this.items.add(item);
    }

    public void addItem(int position, L item) {
        this.items.add(position, item);
    }

    public void addItems(List<L> items) {
        this.items.addAll(items);
    }

    public void addItems(int position, List<L> items) {
        this.items.addAll(position, items);
    }

    public void removeItem(L item) {
        this.items.remove(item);
    }

    public void removeItem(int position) {
        this.items.remove(position);
    }

    public void clear() {
        this.items.clear();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}