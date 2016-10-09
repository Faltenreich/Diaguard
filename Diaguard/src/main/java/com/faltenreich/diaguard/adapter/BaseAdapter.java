package com.faltenreich.diaguard.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.faltenreich.diaguard.ui.view.viewholder.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Filip on 04.11.13.
 */
public abstract class BaseAdapter<L, VH extends BaseViewHolder<L>> extends RecyclerView.Adapter<VH> {

    private Context context;
    private List<L> items;

    public BaseAdapter(Context context) {
        this.context = context;
        this.items = new ArrayList<>();
    }

    protected Context getContext() {
        return context;
    }

    public L getItem(int position) {
        return items.get(position);
    }

    public List<L> getItems() {
        return items;
    }

    public int getItemPosition(L item) {
        return items.indexOf(item);
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

    public void updateItem(int position, L item) {
        items.set(position, item);
    }

    public void clear() {
        this.items.clear();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}