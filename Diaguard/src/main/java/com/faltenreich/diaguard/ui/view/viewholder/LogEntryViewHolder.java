package com.faltenreich.diaguard.ui.view.viewholder;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.list.ListItemEntry;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.BloodSugar;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.EntryTag;
import com.faltenreich.diaguard.data.entity.Insulin;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.data.entity.Tag;
import com.faltenreich.diaguard.event.Events;
import com.faltenreich.diaguard.event.ui.TagSelectedEvent;
import com.faltenreich.diaguard.ui.activity.EntryActivity;
import com.faltenreich.diaguard.ui.view.TintImageView;
import com.pchmn.materialchips.ChipView;

import java.util.List;

import butterknife.BindView;

public class LogEntryViewHolder extends BaseViewHolder<ListItemEntry> implements View.OnClickListener {

    @BindView(R.id.root_layout) LinearLayout rootLayout;
    @BindView(R.id.cardview) CardView cardView;
    @BindView(R.id.time) TextView time;
    @BindView(R.id.note) TextView note;
    @BindView(R.id.measurements) public ViewGroup measurements;
    @BindView(R.id.entry_tags) ViewGroup tagsView;

    public LogEntryViewHolder(View view) {
        super(view);
    }

    @Override
    public void bindData() {
        cardView.setOnClickListener(this);

        Entry entry = getListItem().getEntry();
        List<EntryTag> entryTags = getListItem().getEntryTags();

        time.setText(entry.getDate().toString("HH:mm"));

        if (entry.getNote() != null && entry.getNote().length() > 0) {
            note.setVisibility(View.VISIBLE);
            note.setText(entry.getNote());
        } else {
            note.setVisibility(View.GONE);
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
                    Events.post(new TagSelectedEvent(tag));
                }
            });
            tagsView.addView(chipView);
        }

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            measurements.removeAllViews();
            for (Measurement measurement : entry.getMeasurementCache()) {
                Measurement.Category category = measurement.getCategory();
                View viewMeasurement = inflater.inflate(R.layout.list_item_log_measurement, measurements, false);
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
                measurements.addView(viewMeasurement);
            }
        }
    }

    @Override
    public void onClick(View view) {
        EntryActivity.show(getContext(), getListItem().getEntry());
    }

    @Override
    public int getSwipeFlags() {
        return ItemTouchHelper.START | ItemTouchHelper.END;
    }
}
