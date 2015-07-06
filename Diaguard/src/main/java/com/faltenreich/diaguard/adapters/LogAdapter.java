package com.faltenreich.diaguard.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.database.measurements.Measurement;

/**
 * Created by Filip on 04.11.13.
 */
public class LogAdapter extends BaseAdapter<Measurement, RecyclerView.ViewHolder> {

    private enum ViewType {
        SECTION,
        ENTRY
    }

    private Context context;

    public LogAdapter(Context context) {
        this.context = context;
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
                return new ViewHolderEntry(LayoutInflater.from(context).inflate(R.layout.listview_row_entry, parent, false));
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewType viewType = ViewType.values()[holder.getItemViewType()];
        switch (viewType) {
            case SECTION:
                
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