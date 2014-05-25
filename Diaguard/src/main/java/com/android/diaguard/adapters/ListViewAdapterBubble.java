package com.android.diaguard.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.diaguard.R;
import com.android.diaguard.database.Event;
import com.android.diaguard.helpers.Helper;
import com.android.diaguard.helpers.PreferenceHelper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Filip on 04.11.13.
 */
public class ListViewAdapterBubble extends BaseAdapter {

    private static class ViewHolder {
        TextView time;
        TextView value;
        TextView unit;
    }

    Context context;
    public List<Event> events;
    PreferenceHelper preferenceHelper;

    public ListViewAdapterBubble(Context context){
        this.context = context;
        this.events = new ArrayList<Event>();
        preferenceHelper = new PreferenceHelper((Activity)context);
    }

    public int getCount() {
        return events.size();
    }

    public Event getItem(int position) {
        return events.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null)
        {
            LayoutInflater inflate = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflate.inflate(R.layout.listview_row_bubble, null);

            holder = new ViewHolder();
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.value = (TextView) convertView.findViewById(R.id.value);
            holder.unit = (TextView) convertView.findViewById(R.id.unit);

            convertView.setTag(holder);
        }
        else
            holder = (ViewHolder) convertView.getTag();

        Event event = getItem(position);

        int differenceInMinutes = Helper.getDifferenceInMinutes(event.getDate(), Calendar.getInstance());
        holder.time.setText(Helper.getTextAgo(context, differenceInMinutes));

        float valueFloat = preferenceHelper.formatDefaultToCustomUnit(
                event.getCategory(), event.getValue());
        DecimalFormat format = Helper.getDecimalFormat();
        holder.value.setText(format.format(valueFloat));

        holder.unit.setText(preferenceHelper.getUnitAcronym(event.getCategory()));

        int size = (int)Helper.getDPI(context, 120);
        AbsListView.LayoutParams layoutParams =
                new AbsListView.LayoutParams(size, size);
        convertView.setLayoutParams(layoutParams);
        return convertView;
    }
}