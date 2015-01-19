package com.faltenreich.diaguard.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.faltenreich.diaguard.R;

/**
 * Created by Filip on 06.01.2015.
 */
public class DrawerListViewAdapter extends BaseAdapter {

    private Context context;
    private String[] titles;
    private int[] iconIds;
    private int fragmentCount;

    public DrawerListViewAdapter(Context context) {
        this.context = context;
        this.titles = new String[] {
                context.getString(R.string.home),
                context.getString(R.string.timeline),
                context.getString(R.string.log),
                context.getString(R.string.calculator),
                context.getString(R.string.export),
                context.getString(R.string.settings) };
        this.iconIds = new int[] {
                R.drawable.drawable_dawer_home,
                R.drawable.drawable_dawer_timeline,
                R.drawable.drawable_dawer_log
        };
        this.fragmentCount = iconIds.length;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView;
        if(position < fragmentCount) {
            itemView = inflater.inflate(R.layout.drawer_list_item_fragment, parent, false);
            ImageView imgIcon = (ImageView) itemView.findViewById(R.id.icon);
            imgIcon.setImageDrawable(context.getResources().getDrawable(iconIds[position]));
        }
        else {
            itemView = inflater.inflate(R.layout.drawer_list_item_activity, parent, false);
        }

        TextView txtTitle = (TextView) itemView.findViewById(R.id.title);
        txtTitle.setText(titles[position]);

        return itemView;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Object getItem(int position) {
        return titles[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getFragmentCount() {
        return fragmentCount;
    }
}