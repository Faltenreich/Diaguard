package com.faltenreich.diaguard.adapters;

import android.support.v7.widget.RecyclerView;

import com.faltenreich.diaguard.database.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Filip on 04.11.13.
 */
public abstract class BaseAdapter<T extends Model, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private enum ViewType {
        SECTION,
        ENTRY
    }

    protected List<ListItem> items;

    public BaseAdapter() {
        this.items = new ArrayList<>();
    }

    public void addItem(ListItem item) {
        this.items.add(item);
        notifyDataSetChanged();
    }

    public void addItems(List<ListItem> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void removeItem(ListItem item) {
        this.items.remove(item);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        this.items.remove(position);
        notifyDataSetChanged();
    }

    public void clear() {
        this.items.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) instanceof ListSection ? ViewType.SECTION.ordinal() : ViewType.ENTRY.ordinal();
    }
}