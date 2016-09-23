package com.faltenreich.diaguard.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.ui.view.viewholder.FoodViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Faltenreich on 20.09.2016.
 */

public class FoodAutoCompleteAdapter extends ArrayAdapter<Food> implements Filterable {

    private List<Food> items;

    public FoodAutoCompleteAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1);
        this.items = new ArrayList<>();
        Food food = new Food();
        food.setName("Food");
        items.add(food);
        items.add(food);
        items.add(food);
    }

    public void setItems(List<Food> items) {
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Nullable
    @Override
    public Food getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        FoodViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_auto_complete_food, parent);
            holder = new FoodViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (FoodViewHolder) convertView.getTag();
        }
        holder.bindData(getItem(position));
        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return super.getFilter();
    }
}
