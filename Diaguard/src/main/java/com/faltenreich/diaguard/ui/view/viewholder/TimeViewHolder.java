package com.faltenreich.diaguard.ui.view.viewholder;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.list.ListItemTimePreference;
import com.faltenreich.diaguard.data.TimeInterval;
import com.faltenreich.diaguard.util.DateTimeUtils;
import com.faltenreich.diaguard.util.Helper;
import com.faltenreich.diaguard.util.NumberUtils;

import org.joda.time.DateTimeConstants;

import butterknife.BindView;

/**
 * Created by Faltenreich on 03.09.2016.
 */
public class TimeViewHolder extends BaseViewHolder<ListItemTimePreference> {

    @BindView(R.id.list_item_time_text)
    protected TextView time;

    @BindView(R.id.list_item_time_value)
    protected EditText value;

    public TimeViewHolder(View view) {
        super(view);
    }

    @Override
    protected void bindData() {
        final ListItemTimePreference preference = getListItem();

        if (preference.getInterval() == TimeInterval.CONSTANT) {
            time.setVisibility(View.GONE);
        } else {
            time.setVisibility(View.VISIBLE);
            int hourOfDay = preference.getHourOfDay();
            int target = (preference.getHourOfDay() + preference.getInterval().interval) % DateTimeConstants.HOURS_PER_DAY;
            if (preference.getInterval() == TimeInterval.EVERY_SIX_HOURS) {
                String timeOfDay = getContext().getString(DateTimeUtils.getTimeOfDayResId(hourOfDay));
                time.setText(String.format("%s (%02dh - %02d:00)", timeOfDay, hourOfDay, target));
            } else {
                time.setText(String.format("%02d:00 - %02d:00", hourOfDay, target));
            }
        }

        value.setText(Helper.parseFloatWithDigit(preference.getValue()));
        value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    preference.setValue(NumberUtils.parseNumber(editable.toString()));
                } catch (NumberFormatException exception) {
                    preference.setValue(-1);
                }
            }
        });
    }
}
