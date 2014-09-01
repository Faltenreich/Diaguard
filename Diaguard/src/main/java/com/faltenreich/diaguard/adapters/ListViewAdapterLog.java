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
import com.faltenreich.diaguard.database.Measurement;
import com.faltenreich.diaguard.helpers.Helper;
import com.faltenreich.diaguard.helpers.PreferenceHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Filip on 04.11.13.
 */
public class ListViewAdapterLog extends BaseAdapter {

    private static class ViewHolderRow {
        TextView time;
        LinearLayout values;
    }

    private static class ViewHolderValue {
        TextView value;
        ImageView image;
    }

    private Context context;
    private PreferenceHelper preferenceHelper;

    public List<Entry> entries;
    private HashMap<String, Integer> imageResources;

    public ListViewAdapterLog(Context context){
        this.context = context;
        this.preferenceHelper = new PreferenceHelper(context);

        this.entries = new ArrayList<Entry>();

        // Pre-load image resources
        imageResources = new HashMap<String, Integer>();
        for(Measurement.Category category : Measurement.Category.values()) {
            String name = category.name().toLowerCase();
            int resourceId = context.getResources().getIdentifier(name,
                    "drawable", context.getPackageName());
            imageResources.put(name, resourceId);
        }
    }

    public int getCount() {
        return entries.size();
    }

    public Entry getItem(int position) {
        return entries.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderRow holder;

        if (convertView == null)
        {
            LayoutInflater inflate = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflate.inflate(R.layout.listview_row_log, parent, false);

            holder = new ViewHolderRow();
            holder.time = (TextView)convertView.findViewById(R.id.time);
            holder.values = (LinearLayout)convertView.findViewById(R.id.values);

            convertView.setTag(holder);
        }
        else
            holder = (ViewHolderRow) convertView.getTag();

        Entry entry = getItem(position);

        holder.time.setText(Helper.getTimeFormat().print(entry.getDate()));

        for(Measurement measurement : entry.getMeasurements()) {
            ImageView imageViewImage = new ImageView(context);
            imageViewImage.setImageResource(imageResources.get(measurement.getCategory().name().toLowerCase()));
            holder.values.addView(imageViewImage);

            TextView textViewValue = new TextView(context);
            float value = preferenceHelper.formatDefaultToCustomUnit(
                    measurement.getCategory(), measurement.getValue());
            textViewValue.setText(preferenceHelper.getDecimalFormat(measurement.getCategory()).format(value));

            // Highlight extrema
            if(measurement.getCategory() == Measurement.Category.BloodSugar && preferenceHelper.limitsAreHighlighted()) {
                if(measurement.getValue() > preferenceHelper.getLimitHyperglycemia())
                    textViewValue.setTextColor(context.getResources().getColor(R.color.red));
                else if(measurement.getValue() < preferenceHelper.getLimitHypoglycemia())
                    textViewValue.setTextColor(context.getResources().getColor(R.color.blue));
            }
            holder.values.addView(textViewValue);
        }

        return convertView;
    }
}