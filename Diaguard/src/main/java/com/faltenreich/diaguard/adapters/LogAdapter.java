package com.faltenreich.diaguard.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.database.DatabaseFacade;
import com.faltenreich.diaguard.database.Entry;
import com.faltenreich.diaguard.database.measurements.Measurement;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Filip on 04.11.13.
 */
public class LogAdapter extends BaseAdapter<Measurement, RecyclerView.ViewHolder> {

    private static final int PAGE_SIZE = 20;

    private enum ViewType {
        SECTION,
        ENTRY,
        EMPTY
    }

    private Context context;
    private List<ListItem> items;

    public LogAdapter(Context context) {
        this.context = context;
        this.items = new ArrayList<>();
        fetchData(0);
    }

    private void fetchData(int page) {
        // Group by
        try {
            DatabaseFacade.getInstance().getAll(Entry.class, page * PAGE_SIZE, PAGE_SIZE, Entry.DATE, false);
        } catch (SQLException exception) {
            Log.e("MainFragment", exception.getMessage());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (items.size() == 0) {
            return ViewType.EMPTY.ordinal();
        } else {
            ListItem item = items.get(position);
            if (item instanceof ListSection) {
                return ViewType.SECTION.ordinal();
            } else if (item instanceof ListEntry) {
                return ViewType.ENTRY.ordinal();
            }
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewTypeInt) {
        ViewType viewType = ViewType.values()[viewTypeInt];
        switch (viewType) {
            case SECTION:
                return new ViewHolderSection(LayoutInflater.from(context).inflate(R.layout.listview_row_section, parent, false));
            case ENTRY:
                return new ViewHolderEntry(LayoutInflater.from(context).inflate(R.layout.listview_row_entry, parent, false));
            default:
                return new ViewHolderEmpty(LayoutInflater.from(context).inflate(R.layout.listview_row_entry, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewType viewType = ViewType.values()[holder.getItemViewType()];
        switch (viewType) {
            case SECTION:
                holder = (ViewHolderSection) holder;
                break;
            case ENTRY:
                holder = (ViewHolderEntry) holder;
                break;
        }
    }

    private static class ViewHolderEmpty extends RecyclerView.ViewHolder {
        public ViewHolderEmpty(View view) {
            super(view);
        }
    }

    private static class ViewHolderSection extends RecyclerView.ViewHolder {
        TextView date;
        public ViewHolderSection(View view) {
            super(view);
            this.date = (TextView) view.findViewById(R.id.date);
        }
    }

    private static class ViewHolderEntry extends RecyclerView.ViewHolder {
        TextView time;
        LinearLayout values;
        public ViewHolderEntry(View view) {
            super(view);
            this.time = (TextView) view.findViewById(R.id.time);
            this.values = (LinearLayout) view.findViewById(R.id.values);
        }
    }
}