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
import java.util.List;

/**
 * Created by Faltenreich on 04.01.2018
 */

public class TagAutoCompleteAdapter extends ArrayAdapter<Tag> {

    private List<Tag> tags;
    private List<Tag> results;

    public TagAutoCompleteAdapter(@NonNull Context context, List<Tag> tags) {
        super(context, -1);
        this.tags = tags;
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
                    for (int position = 0; position < tags.size(); position++) {
                        Tag tag = tags.get(position);
                        if (tag != null && tag.getName().contains(constraint)) {
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
                    /* TODO: Find way to prevent showing all tags on IME_ACTION_DONE
                } else if (TextUtils.isEmpty(constraint)) {
                    results = tags;
                    notifyDataSetChanged();
                    */
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
    }
}
