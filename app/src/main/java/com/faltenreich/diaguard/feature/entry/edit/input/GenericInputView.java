package com.faltenreich.diaguard.feature.entry.edit.input;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.ListItemMeasurementGenericBinding;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.data.database.entity.Measurement;
import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;
import com.faltenreich.diaguard.shared.data.primitive.StringUtils;
import com.faltenreich.diaguard.shared.view.edittext.StickyHintInputView;

/**
 * Created by Faltenreich on 20.09.2015.
 */
@SuppressLint("ViewConstructor")
public class GenericInputView<T extends Measurement> extends MeasurementInputView<ListItemMeasurementGenericBinding, T> implements TextWatcher {

    private final StickyHintInputView inputField;

    public GenericInputView(Context context, Class<T> clazz, T measurement) {
        super(context, clazz, measurement);
        inputField = getBinding().inputField;
        inputField.getEditText().setSaveEnabled(false);
    }

    @Override
    protected ListItemMeasurementGenericBinding createBinding(LayoutInflater inflater) {
        return ListItemMeasurementGenericBinding.inflate(inflater, this, true);
    }

    @Override
    protected void onBind(Measurement measurement) {
        inputField.setTag(measurement.getCategory());
        inputField.setHint(PreferenceStore.getInstance().getUnitAcronym(measurement.getCategory()));
        inputField.setText(measurement.getValuesForUI()[0]);
        inputField.getEditText().addTextChangedListener(this);
    }

    @Override
    protected void onUnbind(T measurement) {
        inputField.getEditText().removeTextChangedListener(this);
        super.onUnbind(measurement);
    }

    @Override
    public boolean isValid() {
        boolean isValid;
        String input = inputField.getText();
        if (StringUtils.isBlank(input)) {
            inputField.setError(getContext().getString(R.string.validator_value_empty));
            isValid = false;
        } else {
            isValid = PreferenceStore.getInstance().isValueValid(inputField.getEditText(), getMeasurement().getCategory());
        }
        return isValid;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        T measurement = getMeasurement();
        float value = FloatUtils.parseNumber(editable.toString());
        value = PreferenceStore.getInstance().formatCustomToDefaultUnit(measurement.getCategory(), value);
        measurement.setValues(value);
    }
}