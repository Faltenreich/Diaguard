package com.faltenreich.diaguard.ui.viewholder;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.ListItemEntry;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.BloodSugar;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.Insulin;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.ui.activity.EntryActivity;
import com.faltenreich.diaguard.ui.view.TintImageView;

import org.joda.time.DateTime;

import butterknife.Bind;

/**
 * Created by Faltenreich on 17.10.2015.
 */
public class LogEntryViewHolder extends BaseViewHolder<ListItemEntry> implements View.OnClickListener {

    @Bind(R.id.time)
    public TextView time;

    @Bind(R.id.note)
    public TextView note;

    @Bind(R.id.measurements)
    public ViewGroup measurements;

    private Entry entry;

    public LogEntryViewHolder(View view) {
        super(view);
        view.setOnClickListener(this);
    }

    @Override
    public void bindData(final ListItemEntry listItem) {
        entry = listItem.getEntry();

        time.setText(entry.getDate().toString("HH:mm"));

        if (entry.getNote() != null && entry.getNote().length() > 0) {
            note.setVisibility(View.VISIBLE);
            note.setText(entry.getNote());
        } else {
            note.setVisibility(View.GONE);
        }

        LayoutInflater inflate = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (Measurement measurement : entry.getMeasurementCache()) {
            Measurement.Category category = measurement.getCategory();
            View viewMeasurement = inflate.inflate(R.layout.list_item_log_measurement, measurements, false);
            TintImageView categoryImage = (TintImageView) viewMeasurement.findViewById(R.id.image);
            int imageResourceId = PreferenceHelper.getInstance().getCategoryImageResourceId(category);
            categoryImage.setImageDrawable(ContextCompat.getDrawable(getContext(), imageResourceId));
            categoryImage.setTintColor(ContextCompat.getColor(getContext(), R.color.gray_dark));
            TextView value = (TextView) viewMeasurement.findViewById(R.id.value);

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

    @Override
    public void onClick(View v) {
        if (entry != null) {
            Intent intent = new Intent(getContext(), EntryActivity.class);
            intent.putExtra(EntryActivity.EXTRA_ENTRY, entry.getId());
            getContext().startActivity(intent);
        }
    }
}
