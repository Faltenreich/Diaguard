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
import java.util.HashMap;
import java.util.List;

/**
 * Created by Filip on 04.11.13.
 */
public class ListViewAdapterMain extends BaseAdapter {

    private static class ViewHolder {
        TextView value;
        TextView time;
    }

    Context context;
    public List<Event> events;
    PreferenceHelper preferenceHelper;
    HashMap<String, Integer> imageResources;

    public ListViewAdapterMain(Context context){
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
            convertView = inflate.inflate(R.layout.listview_row_main, null);

            holder = new ViewHolder();
            holder.value = (TextView) convertView.findViewById(R.id.value);
            holder.time = (TextView) convertView.findViewById(R.id.time);

            convertView.setTag(holder);
        }
        else
            holder = (ViewHolder) convertView.getTag();

        Event event = getItem(position);

        float valueFloat = preferenceHelper.formatDefaultToCustomUnit(
                event.getCategory(), event.getValue());
        DecimalFormat format = Helper.getDecimalFormat();
        String value = format.format(valueFloat);

        String unit = preferenceHelper.getUnitAcronym(event.getCategory());

        String info = context.getResources().
                getTextArray(R.array.categories_info)[event.getCategory().ordinal()].toString();

        holder.value.setText(value + " " + unit + " " + info);

        int differenceInMinutes = Helper.getDifferenceInMinutes(event.getDate(), Calendar.getInstance());
        holder.time.setText(Helper.getTextAgo(context, differenceInMinutes));

        AbsListView.LayoutParams layoutParams =
                new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        (int) context.getResources().getDimension(R.dimen.height_element));
        convertView.setLayoutParams(layoutParams);
        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}