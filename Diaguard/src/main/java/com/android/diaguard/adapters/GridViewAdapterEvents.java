package com.android.diaguard.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.diaguard.database.Event;
import com.android.diaguard.helpers.Helper;
import com.android.diaguard.helpers.PreferenceHelper;

import java.text.DecimalFormat;
import java.util.HashMap;

/**
 * Created by Filip on 04.11.13.
 */
public class GridViewAdapterEvents extends BaseAdapter {

    Context context;
    PreferenceHelper preferenceHelper;
    HashMap<String, Integer> imageResources;
    HashMap<Event.Category, float[]> events;
    int eventsPerRow;

    GridViewAdapterEvents(Context context, HashMap<Event.Category, float[]> events, int eventsPerRow){
        this.context = context;
        preferenceHelper = new PreferenceHelper((Activity)context);

        this.events = events;
        this.eventsPerRow = eventsPerRow;

        // Preload image resources to improve performance
        imageResources = new HashMap<String, Integer>();
        for(Event.Category category : Event.Category.values()) {
            String name = category.name().toString().toLowerCase();
            int resourceId = context.getResources().getIdentifier(name,
                    "drawable", context.getPackageName());
            imageResources.put(name, resourceId);
        }
    }

    public int getCount() {
        return events.size();
    }

    public Event getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        TextView textView;
        if (convertView == null)
            textView = new TextView(context);
        else
            textView = (TextView) convertView;

        Event event = getItem(position);

        float valueFloat = preferenceHelper.formatDefaultToCustomUnit(
                event.getCategory(), event.getValue());
        DecimalFormat format = Helper.getDecimalFormat();
        textView.setText(format.format(valueFloat));

        return convertView;
    }
}