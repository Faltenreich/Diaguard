package com.faltenreich.diaguard.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.database.Event;
import com.faltenreich.diaguard.helpers.PreferenceHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Filip on 04.11.13.
 */
public class ListViewAdapterLog extends BaseAdapter {

    private static class ViewHolder {
        ImageView image;
        TextView time;
        TextView unit;
        TextView value;
        View noteInfo;
    }

    Context context;
    public List<Event> events;
    PreferenceHelper preferenceHelper;
    HashMap<String, Integer> imageResources;

    public ListViewAdapterLog(Context context){
        this.context = context;
        this.events = new ArrayList<Event>();

        preferenceHelper = new PreferenceHelper((Activity)context);

        // Pre-load image resources
        imageResources = new HashMap<String, Integer>();
        for(Event.Category category : Event.Category.values()) {
            String name = category.name().toLowerCase();
            int resourceId = context.getResources().getIdentifier(name,
                    "drawable", context.getPackageName());
            imageResources.put(name, resourceId);
        }
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
            convertView = inflate.inflate(R.layout.listview_row_log, parent, false);

            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.time = (TextView)convertView.findViewById(R.id.time);
            holder.unit = (TextView)convertView.findViewById(R.id.unit);
            holder.value = (TextView)convertView.findViewById(R.id.value);
            holder.noteInfo = convertView.findViewById(R.id.notes);

            convertView.setTag(holder);
        }
        else
            holder = (ViewHolder) convertView.getTag();

        Event event = getItem(position);

        holder.image.setImageResource(imageResources.get(event.getCategory().name().toLowerCase()));

        holder.time.setText(preferenceHelper.getTimeFormat().format(event.getDate().getTime()));

        holder.unit.setText(preferenceHelper.getUnitAcronym(event.getCategory()));

        float value = preferenceHelper.formatDefaultToCustomUnit(
                event.getCategory(), event.getValue());
        holder.value.setText(preferenceHelper.getDecimalFormat(event.getCategory()).format(value));

        // Highlighting
        holder.value.setTextColor(Color.BLACK);
        if(event.getCategory() == Event.Category.BloodSugar && preferenceHelper.limitsAreHighlighted()) {
            if(event.getValue() > preferenceHelper.getLimitHyperglycemia())
                holder.value.setTextColor(context.getResources().getColor(R.color.red));
            else if(event.getValue() < preferenceHelper.getLimitHypoglycemia())
                holder.value.setTextColor(context.getResources().getColor(R.color.blue));
        }

        if(event.getNotes().length() > 0)
            holder.noteInfo.setVisibility(View.VISIBLE);

        return convertView;
    }
}