package com.faltenreich.diaguard.feature.log.entry;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.ListItemLogEntryBinding;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.data.database.entity.BloodSugar;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Entry;
import com.faltenreich.diaguard.shared.data.database.entity.EntryTag;
import com.faltenreich.diaguard.shared.data.database.entity.FoodEaten;
import com.faltenreich.diaguard.shared.data.database.entity.Measurement;
import com.faltenreich.diaguard.shared.data.database.entity.Tag;
import com.faltenreich.diaguard.shared.view.chip.ChipView;
import com.faltenreich.diaguard.shared.view.recyclerview.viewholder.BaseViewHolder;
import com.google.android.material.chip.ChipGroup;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class LogEntryViewHolder extends BaseViewHolder<ListItemLogEntryBinding, LogEntryListItem> {

    public interface Listener {
        void onEntrySelected(Entry entry);
        void onTagSelected(Tag tag, View view);
        void onDateSelected(DateTime dateTime);
    }

    private final Listener listener;

    public LogEntryViewHolder(ViewGroup parent, Listener listener) {
        super(parent, R.layout.list_item_log_entry);
        this.listener = listener;
        getBinding().container.setOnClickListener(view -> listener.onEntrySelected(getItem().getEntry()));
    }

    @Override
    protected ListItemLogEntryBinding createBinding(View view) {
        return ListItemLogEntryBinding.bind(view);
    }

    @Override
    public void onBind(LogEntryListItem item) {
        Entry entry = item.getEntry();
        List<EntryTag> entryTags = item.getEntryTags();
        List<FoodEaten> foodEatenList = item.getFoodEatenList();

        getBinding().dateLabel.setText(entry.getDate().toString("HH:mm"));

        TextView noteLabel = getBinding().noteLabel;
        if (entry.getNote() != null && entry.getNote().length() > 0) {
            noteLabel.setVisibility(View.VISIBLE);
            noteLabel.setText(entry.getNote());
        } else {
            noteLabel.setVisibility(View.GONE);
        }

        TextView foodLabel = getBinding().foodLabel;
        if (foodEatenList != null && foodEatenList.size() > 0) {
            List<String> foodNotes = new ArrayList<>();
            for (FoodEaten foodEaten : foodEatenList) {
                String foodEatenAsString = foodEaten.print();
                if (foodEatenAsString != null) {
                    foodNotes.add(foodEatenAsString);
                }
            }
            if (foodNotes.size() > 0) {
                foodLabel.setVisibility(View.VISIBLE);
                foodLabel.setText(TextUtils.join("\n", foodNotes));
            } else {
                foodLabel.setVisibility(View.GONE);
            }
        } else {
            foodLabel.setVisibility(View.GONE);
        }

        ChipGroup entryTagChipGroup = getBinding().entryTagChipGroup;
        entryTagChipGroup.setVisibility(entryTags != null && entryTags.size() > 0 ? View.VISIBLE : View.GONE);
        entryTagChipGroup.removeAllViews();
        if (entryTags != null) {
            for (EntryTag entryTag : entryTags) {
                Tag tag = entryTag.getTag();
                if (tag != null) {
                    ChipView chipView = new ChipView(getContext());
                    chipView.setText(tag.getName());
                    chipView.setOnClickListener(view -> listener.onTagSelected(tag, view));
                    entryTagChipGroup.addView(chipView);
                }
            }
        }

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout measurementsLayout = getBinding().measurementsLayout;
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

    public void onRecycle() {
        getBinding().measurementsLayout.removeAllViews();
    }

    @Override
    public int getSwipeFlags() {
        return ItemTouchHelper.START | ItemTouchHelper.END;
    }
}
