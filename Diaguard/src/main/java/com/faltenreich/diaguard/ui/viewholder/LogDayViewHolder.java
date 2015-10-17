package com.faltenreich.diaguard.ui.viewholder;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.ListItem;
import com.faltenreich.diaguard.adapter.LogListEntry;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.BloodSugar;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.Insulin;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.ui.activity.EntryDetailActivity;
import com.faltenreich.diaguard.ui.activity.NewEventActivity;
import com.faltenreich.diaguard.ui.fragments.EntryDetailFragment;

import org.joda.time.DateTime;

import butterknife.Bind;

/**
 * Created by Faltenreich on 17.10.2015.
 */
public class LogDayViewHolder extends BaseViewHolder<LogListEntry> {

    @Bind(R.id.day)
    public TextView day;

    @Bind(R.id.weekday)
    public TextView weekDay;

    @Bind(R.id.entries)
    public ViewGroup entries;

    public LogDayViewHolder(View view) {
        super(view);
    }

    @Override
    public void bindData(final LogListEntry recyclerEntry) {
        LayoutInflater inflate = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        day.setText(recyclerEntry.getDateTime().toString("dd"));
        weekDay.setText(recyclerEntry.getDateTime().dayOfWeek().getAsShortText());

        // Highlight current day
        boolean isToday = recyclerEntry.getDateTime().withTimeAtStartOfDay().isEqual(DateTime.now().withTimeAtStartOfDay());
        int textColor =  isToday ?
                ContextCompat.getColor(getContext(), R.color.green) :
                ContextCompat.getColor(getContext(), R.color.gray_dark);
        day.setTextColor(textColor);
        weekDay.setTextColor(textColor);

        // TODO: Threading
        if (recyclerEntry.hasEntries()) {
            for (final Entry entry : recyclerEntry.getEntries()) {
                View viewEntry = inflate.inflate(R.layout.list_item_log_entry, entries, false);
                viewEntry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), EntryDetailActivity.class);
                        intent.putExtra(EntryDetailFragment.EXTRA_ENTRY, entry.getId());
                        getContext().startActivity(intent);
                    }
                });

                TextView time = (TextView) viewEntry.findViewById(R.id.time);
                time.setText(entry.getDate().toString("HH:mm"));

                TextView viewNote = (TextView) viewEntry.findViewById(R.id.note);
                if (entry.getNote() != null && entry.getNote().length() > 0) {
                    viewNote.setVisibility(View.VISIBLE);
                    viewNote.setText(entry.getNote());
                } else {
                    viewNote.setVisibility(View.GONE);
                }

                ViewGroup layoutEntries = (ViewGroup) viewEntry.findViewById(R.id.measurements);
                for (Measurement measurement : entry.getMeasurementCache()) {
                    Measurement.Category category = measurement.getCategory();
                    View viewMeasurement = inflate.inflate(R.layout.list_item_log_measurement, entries, false);
                    ImageView categoryImage = (ImageView) viewMeasurement.findViewById(R.id.image);
                    int imageResourceId = PreferenceHelper.getInstance().getCategoryImageResourceId(category);
                    categoryImage.setImageDrawable(ContextCompat.getDrawable(getContext(), imageResourceId));
                    categoryImage.setColorFilter(ContextCompat.getColor(getContext(), R.color.gray_dark), PorterDuff.Mode.SRC_ATOP.SRC_ATOP);
                    TextView value = (TextView) viewMeasurement.findViewById(R.id.value);

                    switch (category) {
                        case INSULIN:
                            value.setText(((Insulin)measurement).toStringDetail());
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
                                categoryImage.setColorFilter(backgroundColor, PorterDuff.Mode.SRC_ATOP.SRC_ATOP);
                            }
                        default:
                            value.setText(measurement.toString() + " " + PreferenceHelper.getInstance().getUnitAcronym(category));
                    }

                    layoutEntries.addView(viewMeasurement);
                }

                entries.addView(viewEntry);
            }
        } else {
            View view = inflate.inflate(R.layout.list_item_log_empty, entries, false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), NewEventActivity.class);
                    intent.putExtra(NewEventActivity.EXTRA_DATE, recyclerEntry.getDateTime());
                    getContext().startActivity(intent);
                }
            });
            entries.addView(view);
        }

        // Add indicator behind last entry
        if (isToday) {
            View view = inflate.inflate(R.layout.list_item_log_indicator, entries, false);
            entries.addView(view);
        }
    }
}
