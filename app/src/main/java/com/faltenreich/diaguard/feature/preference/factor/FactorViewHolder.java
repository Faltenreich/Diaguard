package com.faltenreich.diaguard.feature.preference.factor;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.ViewGroup;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.preference.data.Daytime;
import com.faltenreich.diaguard.feature.preference.data.TimeInterval;
import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;
import com.faltenreich.diaguard.shared.view.edittext.StickyHintInput;
import com.faltenreich.diaguard.shared.view.recyclerview.viewholder.BaseViewHolder;

import org.joda.time.DateTimeConstants;

import butterknife.BindView;

class FactorViewHolder extends BaseViewHolder<FactorListItem> implements TextWatcher {

    @BindView(R.id.inputField) StickyHintInput inputField;

    FactorViewHolder(ViewGroup parent) {
        super(parent, R.layout.list_item_factor);
        inputField.getInputView().addTextChangedListener(this);
    }

    @Override
    protected void onBind(FactorListItem item) {
        inputField.getInputView().setText(item.getValue() >= 0
            ? FloatUtils.parseFloat(item.getValue())
            : null);

        int hourOfDay = item.getHourOfDay();
        int target = (item.getHourOfDay() + item.getInterval().interval) % DateTimeConstants.HOURS_PER_DAY;
        if (item.getInterval() == TimeInterval.EVERY_SIX_HOURS) {
            String timeOfDay = getContext().getString(Daytime.toDayTime(hourOfDay).textResourceId);
            inputField.setHint(String.format("%s (%02d - %02d:00)", timeOfDay, hourOfDay, target));
        } else {
            inputField.setHint(String.format("%02d:00 - %02d:00", hourOfDay, target));
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        FactorListItem preference = getItem();
        try {
            preference.setValue(FloatUtils.parseNumber(editable.toString()));
        } catch (NumberFormatException exception) {
            preference.setValue(-1);
        }
    }
}
