package com.faltenreich.diaguard.shared.view.recyclerview.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.faltenreich.diaguard.shared.view.recyclerview.viewholder.BaseViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Filip on 04.11.13.
 */
public abstract class BaseAdapter<ITEM, HOLDER extends BaseViewHolder<?, ITEM>> extends RecyclerView.Adapter<HOLDER> {

    private final Context context;
    private final List<ITEM> items;

    public BaseAdapter(Context context) {
        this.context = context;
        this.items = new ArrayList<>();
    }

    protected Context getContext() {
        return context;
    }

    public ITEM getItem(int position) {
        return this.items.get(position);
    }

    public List<ITEM> getItems() {
        return this.items;
    }

    public void setItem(ITEM item, int position) {
        if (position >= 0 && position < items.size()) {
            items.set(position, item);
        }
    }

    public int getItemPosition(ITEM item) {
        return this.items.indexOf(item);
    }

    public void addItem(ITEM item) {
        this.items.add(item);
    }

    public void addItem(int position, ITEM item) {
        this.items.add(position, item);
    }

    public void addItems(List<ITEM> items) {
        this.items.addAll(items);
    }

    public void addItems(ITEM[] items) {
        this.items.addAll(Arrays.asList(items));
    }

    public void addItems(int position, List<ITEM> items) {
        this.items.addAll(position, items);
    }

    public void removeItem(ITEM item) {
        this.items.remove(item);
    }

    public void removeItem(int position) {
        this.items.remove(position);
    }

    public void updateItem(int position, ITEM item) {
        this.items.set(position, item);
    }

    public void clear() {
        this.items.clear();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}