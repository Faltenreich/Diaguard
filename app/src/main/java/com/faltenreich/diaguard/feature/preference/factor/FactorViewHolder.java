package com.faltenreich.diaguard.feature.preference.factor;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;
import com.faltenreich.diaguard.shared.view.edittext.LocalizedNumberEditText;
import com.faltenreich.diaguard.shared.view.recyclerview.viewholder.BaseViewHolder;

import org.joda.time.DateTimeConstants;

import butterknife.BindView;

/**
 * Created by Faltenreich on 03.09.2016.
 */
public class FactorViewHolder extends BaseViewHolder<FactorListItem> {

    @BindView(R.id.list_item_time_text)
    protected TextView time;

    @BindView(R.id.list_item_time_value)
    public LocalizedNumberEditText value;

    public FactorViewHolder(ViewGroup parent) {
        super(parent, R.layout.list_item_factor);
    }

    @Override
    protected void bindData() {
        final FactorListItem preference = getListItem();

        if (preference.getInterval() == TimeInterval.CONSTANT) {
            time.setVisibility(View.GONE);
        } else {
            time.setVisibility(View.VISIBLE);
            int hourOfDay = preference.getHourOfDay();
            int target = (preference.getHourOfDay() + preference.getInterval().interval) % DateTimeConstants.HOURS_PER_DAY;
            if (preference.getInterval() == TimeInterval.EVERY_SIX_HOURS) {
                String timeOfDay = getContext().getString(Daytime.toDayTime(hourOfDay).textResourceId);
                time.setText(String.format("%s (%02d - %02d:00)", timeOfDay, hourOfDay, target));
            } else {
                time.setText(String.format("%02d:00 - %02d:00", hourOfDay, target));
            }
        }

        value.setText(preference.getValue() >= 0 ? FloatUtils.parseFloat(preference.getValue()) : null);
    }
}
