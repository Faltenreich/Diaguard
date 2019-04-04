package com.faltenreich.diaguard.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import com.faltenreich.diaguard.ui.view.viewholder.BaseViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Filip on 04.11.13.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
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
        return this.items.get(position);
    }

    public List<L> getItems() {
        return this.items;
    }

    public boolean setItem(L item, int position) {
        if (position >= 0 && position < items.size()) {
            items.set(position, item);
            return true;
        }
        return false;
    }

    public int getItemPosition(L item) {
        return this.items.indexOf(item);
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

    public void addItems(L[] items) {
        this.items.addAll(Arrays.asList(items));
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