package com.faltenreich.diaguard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.list.ListItemTimePreference;
import com.faltenreich.diaguard.ui.view.viewholder.TimeViewHolder;

/**
 * Created by Faltenreich on 04.09.2016.
 */
public class TimeAdapter extends BaseAdapter<ListItemTimePreference, TimeViewHolder> {

    public TimeAdapter(Context context) {
        super(context);
    }

    @Override
    public TimeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TimeViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.list_item_time, parent, false));
    }

    @Override
    public void onBindViewHolder(TimeViewHolder holder, int position) {
        holder.bindData(getItem(position));
    }
}
