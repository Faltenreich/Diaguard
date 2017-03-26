package com.faltenreich.diaguard.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.list.ListItemChangelog;
import com.faltenreich.diaguard.ui.view.viewholder.ChangelogViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Faltenreich on 26.03.2017
 */

public class ChangelogAdapter extends RecyclerView.Adapter<ChangelogViewHolder> {

    private Context context;
    private List<ListItemChangelog> items;

    public ChangelogAdapter(Context context, String[] items) {
        this.context = context;
        List<ListItemChangelog> listItems = new ArrayList<>();
        for (String item : items) {
            listItems.add(new ListItemChangelog(item));
        }
        this.items = listItems;
    }

    @Override
    public ChangelogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChangelogViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_changelog, parent, false));
    }

    @Override
    public void onBindViewHolder(ChangelogViewHolder holder, int position) {
        holder.bindData(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
