package com.faltenreich.diaguard.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.database.Entry;
import com.faltenreich.diaguard.database.measurements.Measurement;
import com.faltenreich.diaguard.helpers.Helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Filip on 04.11.13.
 */
public class LogBaseAdapter extends BaseAdapter {

    private static class ViewHolderSection {
        TextView date;
    }

    private static class ViewHolderEntry {
        TextView time;
        LinearLayout values;
    }

    private Context context;

    public List<ListItem> items;
    private HashMap<String, Integer> imageResources;

    public LogBaseAdapter(Context context){
        this.context = context;

        this.items = new ArrayList<>();

        // Pre-load image resources
        imageResources = new HashMap<>();
        for(Measurement.Category category : Measurement.Category.values()) {
            String name = category.name().toLowerCase();
            int resourceId = context.getResources().getIdentifier(name,
                    "drawable", context.getPackageName());
            imageResources.put(name, resourceId);
        }
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

    public int getItemPosition(Entry entry) {
        for(int position = 0; position < items.size(); position++) {
            ListItem listItem = items.get(position);
            if(!listItem.isSection()) {
                ListEntry listEntry = (ListEntry) listItem;
                if(listEntry.getEntry().getId() == entry.getId()) {
                    return position;
                }
            }
        }
        return -1;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ListItem listItem = items.get(position);
        if(listItem != null) {
            ViewHolderEntry viewHolderEntry;
            ViewHolderSection viewHolderSection;
            if (listItem.isSection()) {
                if (convertView == null || !(convertView.getTag() instanceof ViewHolderSection)) {
                    LayoutInflater inflate = (LayoutInflater) context.
                            getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflate.inflate(R.layout.listview_row_section, parent, false);
                    convertView.setEnabled(false);

                    viewHolderSection = new ViewHolderSection();
                    viewHolderSection.date = (TextView) convertView.findViewById(R.id.date);
                    convertView.setTag(viewHolderSection);
                }
                else {
                    viewHolderSection = (ViewHolderSection) convertView.getTag();
                }
                ListSection listSection = (ListSection) listItem;
                viewHolderSection.date.setText(listSection.getSectionName());
            }
            else {
                if (convertView == null || !(convertView.getTag() instanceof ViewHolderEntry)) {
                    LayoutInflater inflate = (LayoutInflater) context.
                            getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflate.inflate(R.layout.listview_row_entry, parent, false);

                    viewHolderEntry = new ViewHolderEntry();
                    viewHolderEntry.time = (TextView) convertView.findViewById(R.id.time);
                    viewHolderEntry.values = (LinearLayout) convertView.findViewById(R.id.values);
                    convertView.setTag(viewHolderEntry);
                }
                else {
                    viewHolderEntry = (ViewHolderEntry) convertView.getTag();
                }

                ListEntry listEntry = (ListEntry) listItem;
                Entry entry = listEntry.getEntry();

                viewHolderEntry.time.setText(Helper.getTimeFormat().print(entry.getDate()));

                viewHolderEntry.values.removeAllViews();

                for(Measurement measurement : entry.getMeasurements()) {
                    ImageView imageViewImage = new ImageView(context);
                    imageViewImage.setImageResource(imageResources.get(measurement.getMeasurementType().name().toLowerCase()));
                    viewHolderEntry.values.addView(imageViewImage);

                    /*
                    TextView textViewValue = new TextView(context);
                    float value = PreferenceHelper.getInstance().formatDefaultToCustomUnit(
                            measurement.getMeasurementType(), measurement.getValue());
                    textViewValue.setText(PreferenceHelper.getInstance().getDecimalFormat(measurement.getMeasurementType()).format(value));

                    // Highlight extrema
                    if(measurement.getMeasurementType() == Measurement.Category.BloodSugar && PreferenceHelper.getInstance(.limitsAreHighlighted()) {
                        if(measurement.getValue() > PreferenceHelper.getInstance().getLimitHyperglycemia())
                            textViewValue.setTextColor(context.getResources().getColor(R.color.red));
                        else if(measurement.getValue() < PreferenceHelper.getInstance() .getLimitHypoglycemia())
                            textViewValue.setTextColor(context.getResources().getColor(R.color.blue));
                    }
                    viewHolderEntry.values.addView(textViewValue);
                    */
                }
            }
        }

        return convertView;
    }
}