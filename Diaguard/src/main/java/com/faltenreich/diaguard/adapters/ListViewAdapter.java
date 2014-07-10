package com.faltenreich.diaguard.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.faltenreich.diaguard.R;

import java.util.List;

/**
 * Created by Filip on 04.11.13.
 */
public class ListViewAdapter extends BaseAdapter {

    private static class ViewHolder {
        TextView label;
        TextView value;
    }

    private List<ListItem> items;
    private Context context;

    public ListViewAdapter(Context context, List<ListItem> statistics){
        this.context = context;
        this.items = statistics;
    }

    public int getCount() {
        return items.size();
    }

    public ListItem getItem(int position) {
        return items.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null)
        {
            LayoutInflater inflate =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if(items.get(position).isSection()) {
                convertView = inflate.inflate(R.layout.listview_section, parent, false);
            } else {
                convertView = inflate.inflate(R.layout.listview_row, parent, false);
                if(position % 2 == 0) {
                    convertView.setBackgroundColor(context.getResources().getColor(android.R.color.white));
                }
            }

            holder = new ViewHolder();
            holder.label = (TextView) convertView.findViewById(R.id.label);
            holder.value = (TextView) convertView.findViewById(R.id.value);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.label.setText(items.get(position).getLabel());
        holder.value.setText(items.get(position).getValue());

        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}