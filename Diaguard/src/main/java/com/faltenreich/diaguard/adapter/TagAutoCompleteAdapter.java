package com.faltenreich.diaguard.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.entity.Tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TagAutoCompleteAdapter extends ArrayAdapter<Tag> {

    private HashMap<Tag, Boolean> tags;
    private List<Tag> results;

    public TagAutoCompleteAdapter(@NonNull Context context, List<Tag> tags) {
        super(context, -1, tags);
        this.tags = new HashMap<>();
        for (Tag tag : tags) {
            this.tags.put(tag, true);
        }
        this.results = tags;
    }

    @Override
    public int getCount() {
        return results.size();
    }

    @Nullable
    @Override
    public Tag getItem(int position) {
        return results.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_tag, parent, false);
        }
        TextView characterView = view.findViewById(R.id.tag_character);
        TextView nameView = view.findViewById(R.id.tag_name);

        Tag tag = getItem(position);
        if (tag != null && !TextUtils.isEmpty(tag.getName())) {
            characterView.setText(tag.getName().substring(0, 1).toUpperCase());
            nameView.setText(tag.getName());
        }
        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                List<Tag> suggestions = new ArrayList<>();
                if (!TextUtils.isEmpty(constraint)) {
                    for (Map.Entry<Tag, Boolean> entry : tags.entrySet()) {
                        Tag tag = entry.getKey();
                        if (tag != null && tag.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            suggestions.add(tag);
                        }
                    }
                }
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults filterResults) {
                if (filterResults != null && filterResults.count > 0) {
                    results = (List<Tag>) filterResults.values;
                    notifyDataSetChanged();
                } else if (TextUtils.isEmpty(constraint)) {
                    results = new ArrayList<>();
                    for (Map.Entry<Tag, Boolean> entry : tags.entrySet()) {
                        if (entry.getValue()) {
                            results.add(entry.getKey());
                        }
                    }
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
    }

    public void setTag(Tag tag, boolean enable) {
        if (tags.containsKey(tag)) {
            // FIXME: notifyDataSetChanged does not work
            tags.put(tag, enable);
        }
    }

    public Tag findTag(String name) {
        for (Tag tag : tags.keySet()) {
            if (tag.getName().equalsIgnoreCase(name)) {
                return tag;
            }
        }
        return null;
    }
}
