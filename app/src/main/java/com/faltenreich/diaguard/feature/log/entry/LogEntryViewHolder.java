package com.faltenreich.diaguard.feature.log.entry;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.ListItemLogEntryBinding;
import com.faltenreich.diaguard.feature.entry.edit.EntryEditActivity;
import com.faltenreich.diaguard.feature.entry.search.EntrySearchListAdapter;
import com.faltenreich.diaguard.shared.data.database.entity.BloodSugar;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Entry;
import com.faltenreich.diaguard.shared.data.database.entity.EntryTag;
import com.faltenreich.diaguard.shared.data.database.entity.FoodEaten;
import com.faltenreich.diaguard.shared.data.database.entity.Measurement;
import com.faltenreich.diaguard.shared.data.database.entity.Tag;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.view.chip.ChipView;
import com.faltenreich.diaguard.shared.view.recyclerview.viewholder.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class LogEntryViewHolder extends BaseViewHolder<ListItemLogEntryBinding, LogEntryListItem> {

    @BindView(R.id.root_layout) protected ViewGroup rootLayout;
    @BindView(R.id.cardview) protected CardView cardView;
    @BindView(R.id.date_time_view) protected TextView dateTimeView;
    @BindView(R.id.note_view) protected TextView noteView;
    @BindView(R.id.food_view) protected TextView foodView;
    @BindView(R.id.measurements_layout) public ViewGroup measurementsLayout;
    @BindView(R.id.entry_tags) protected ViewGroup tagsView;

    private final EntrySearchListAdapter.OnSearchItemClickListener listener;

    public LogEntryViewHolder(
        ViewGroup parent,
        EntrySearchListAdapter.OnSearchItemClickListener listener
    ) {
        super(parent, R.layout.list_item_log_entry);
        this.listener = listener;
    }

    @Override
    protected ListItemLogEntryBinding createBinding(View view) {
        return ListItemLogEntryBinding.bind(view);
    }

    @Override
    public void onBind(LogEntryListItem item) {
        final Entry entry = item.getEntry();
        final List<EntryTag> entryTags = item.getEntryTags();
        final List<FoodEaten> foodEatenList = item.getFoodEatenList();

        cardView.setOnClickListener(view -> EntryEditActivity.show(getContext(), entry));

        dateTimeView.setText(entry.getDate().toString("HH:mm"));

        if (entry.getNote() != null && entry.getNote().length() > 0) {
            noteView.setVisibility(View.VISIBLE);
            noteView.setText(entry.getNote());
        } else {
            noteView.setVisibility(View.GONE);
        }

        if (foodEatenList != null && foodEatenList.size() > 0) {
            List<String> foodNotes = new ArrayList<>();
            for (FoodEaten foodEaten : foodEatenList) {
                String foodEatenAsString = foodEaten.print();
                if (foodEatenAsString != null) {
                    foodNotes.add(foodEatenAsString);
                }
            }
            if (foodNotes.size() > 0) {
                foodView.setVisibility(View.VISIBLE);
                foodView.setText(TextUtils.join("\n", foodNotes));
            } else {
                foodView.setVisibility(View.GONE);
            }
        } else {
            foodView.setVisibility(View.GONE);
        }

        tagsView.setVisibility(entryTags.size() > 0 ? View.VISIBLE : View.GONE);
        tagsView.removeAllViews();
        for (EntryTag entryTag : entryTags) {
            final Tag tag = entryTag.getTag();
            if (tag != null) {
                ChipView chipView = new ChipView(getContext());
                chipView.setText(tag.getName());
                chipView.setOnClickListener(view -> listener.onTagClicked(tag, view));
                tagsView.addView(chipView);
            }
        }

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            measurementsLayout.removeAllViews();
            List<Measurement> measurements = entry.getMeasurementCache();
            if (measurements.size() > 0) {
                measurementsLayout.setVisibility(View.VISIBLE);
                for (Measurement measurement : measurements) {
                    Category category = measurement.getCategory();
                    View viewMeasurement = inflater.inflate(R.layout.list_item_log_measurement, measurementsLayout, false);
                    ImageView categoryImage = viewMeasurement.findViewById(R.id.image);
                    int imageResourceId = category.getIconImageResourceId();
                    categoryImage.setImageDrawable(ContextCompat.getDrawable(getContext(), imageResourceId));
                    categoryImage.setColorFilter(ContextCompat.getColor(getContext(), R.color.gray_dark));
                    TextView value = viewMeasurement.findViewById(R.id.value);
                    value.setText(measurement.print(getContext()));

                    if (category == Category.BLOODSUGAR) {
                        BloodSugar bloodSugar = (BloodSugar) measurement;
                        if (PreferenceStore.getInstance().limitsAreHighlighted()) {
                            int backgroundColor = ContextCompat.getColor(getContext(), R.color.green);
                            if (bloodSugar.getMgDl() > PreferenceStore.getInstance().getLimitHyperglycemia()) {
                                backgroundColor = ContextCompat.getColor(getContext(), R.color.red);
                            } else if (bloodSugar.getMgDl() < PreferenceStore.getInstance().getLimitHypoglycemia()) {
                                backgroundColor = ContextCompat.getColor(getContext(), R.color.blue);
                            }
                            categoryImage.setColorFilter(backgroundColor);
                        }
                    }
                    measurementsLayout.addView(viewMeasurement);
                }
            } else {
                measurementsLayout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getSwipeFlags() {
        return ItemTouchHelper.START | ItemTouchHelper.END;
    }
}
