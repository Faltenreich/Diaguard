package com.faltenreich.diaguard.ui.view.viewholder;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.SearchAdapter;
import com.faltenreich.diaguard.adapter.list.ListItemEntry;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.BloodSugar;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.EntryTag;
import com.faltenreich.diaguard.data.entity.FoodEaten;
import com.faltenreich.diaguard.data.entity.Insulin;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.data.entity.Tag;
import com.faltenreich.diaguard.ui.activity.EntryActivity;
import com.faltenreich.diaguard.ui.view.TintImageView;
import com.pchmn.materialchips.ChipView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class LogEntryViewHolder extends BaseViewHolder<ListItemEntry> {

    @BindView(R.id.root_layout) ViewGroup rootLayout;
    @BindView(R.id.cardview) CardView cardView;
    @BindView(R.id.date_time_view) TextView dateTimeView;
    @BindView(R.id.note_view) TextView noteView;
    @BindView(R.id.food_view) TextView foodView;
    @BindView(R.id.measurements_layout) public ViewGroup measurementsLayout;
    @BindView(R.id.entry_tags) ViewGroup tagsView;

    private SearchAdapter.OnSearchItemClickListener listener;

    public LogEntryViewHolder(View view, SearchAdapter.OnSearchItemClickListener listener) {
        super(view);
        this.listener = listener;
    }

    @Override
    public void bindData() {
        final ListItemEntry listItem = getListItem();
        final Entry entry = listItem.getEntry();
        final List<EntryTag> entryTags = listItem.getEntryTags();
        final List<FoodEaten> foodEatenList = listItem.getFoodEatenList();

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EntryActivity.show(getContext(), entry);
            }
        });

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
        int margin = (int) getContext().getResources().getDimension(R.dimen.padding);
        for (EntryTag entryTag : entryTags) {
            final Tag tag = entryTag.getTag();
            ChipView chipView = new ChipView(getContext());
            chipView.setLabel(tag.getName());
            chipView.setPadding(0, 0, margin, margin);
            chipView.setOnChipClicked(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onTagClicked(tag, view);
                }
            });
            tagsView.addView(chipView);
        }

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            measurementsLayout.removeAllViews();
            List<Measurement> measurements = entry.getMeasurementCache();
            if (measurements.size() > 0) {
                measurementsLayout.setVisibility(View.VISIBLE);
                for (Measurement measurement : measurements) {
                    Measurement.Category category = measurement.getCategory();
                    View viewMeasurement = inflater.inflate(R.layout.list_item_log_measurement, measurementsLayout, false);
                    TintImageView categoryImage = viewMeasurement.findViewById(R.id.image);
                    int imageResourceId = PreferenceHelper.getInstance().getCategoryImageResourceId(category);
                    categoryImage.setImageDrawable(ContextCompat.getDrawable(getContext(), imageResourceId));
                    categoryImage.setTintColor(ContextCompat.getColor(getContext(), R.color.gray_dark));
                    TextView value = viewMeasurement.findViewById(R.id.value);

                    switch (category) {
                        case INSULIN:
                            value.setText(((Insulin) measurement).toStringDetail());
                            break;
                        case BLOODSUGAR:
                            BloodSugar bloodSugar = (BloodSugar) measurement;
                            if (PreferenceHelper.getInstance().limitsAreHighlighted()) {
                                int backgroundColor = ContextCompat.getColor(getContext(), R.color.green);
                                if (bloodSugar.getMgDl() > PreferenceHelper.getInstance().getLimitHyperglycemia()) {
                                    backgroundColor = ContextCompat.getColor(getContext(), R.color.red);
                                } else if (bloodSugar.getMgDl() < PreferenceHelper.getInstance().getLimitHypoglycemia()) {
                                    backgroundColor = ContextCompat.getColor(getContext(), R.color.blue);
                                }
                                categoryImage.setTintColor(backgroundColor);
                            }
                        default:
                            value.setText(String.format("%s %s",
                                    measurement.toString(),
                                    PreferenceHelper.getInstance().getUnitAcronym(category)));
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
