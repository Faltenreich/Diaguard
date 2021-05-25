package com.faltenreich.diaguard.feature.entry.edit.input;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;

import com.faltenreich.diaguard.databinding.ListItemMeasurementPressureBinding;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Pressure;
import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;
import com.faltenreich.diaguard.shared.view.edittext.EditTextUtils;
import com.faltenreich.diaguard.shared.view.edittext.StickyHintInputView;

/**
 * Created by Faltenreich on 20.09.2015.
 */
@SuppressLint("ViewConstructor")
public class PressureInputView extends MeasurementInputView<ListItemMeasurementPressureBinding, Pressure> {

    private final StickyHintInputView systolicInputField;
    private final StickyHintInputView diastolicInputField;

    public PressureInputView(Context context, Pressure pressure) {
        super(context, Pressure.class, pressure);
        systolicInputField = getBinding().systolicInputField;
        systolicInputField.getEditText().setSaveEnabled(false);
        diastolicInputField = getBinding().diastolicInputField;
        diastolicInputField.getEditText().setSaveEnabled(false);
    }

    @Override
    protected ListItemMeasurementPressureBinding createBinding(LayoutInflater inflater) {
        return ListItemMeasurementPressureBinding.inflate(inflater, this, true);
    }

    @Override
    protected void onBind(Pressure measurement) {
        systolicInputField.setText(measurement.getValuesForUI()[0]);
        EditTextUtils.afterTextChanged(systolicInputField.getEditText(), () ->
            measurement.setSystolic(PreferenceStore.getInstance().formatCustomToDefaultUnit(
                measurement.getCategory(),
                FloatUtils.parseNumber(systolicInputField.getText())))
        );

        diastolicInputField.setText(measurement.getValuesForUI()[1]);
        EditTextUtils.afterTextChanged(diastolicInputField.getEditText(), () ->
            measurement.setDiastolic(PreferenceStore.getInstance().formatCustomToDefaultUnit(
                measurement.getCategory(),
                FloatUtils.parseNumber(diastolicInputField.getText())))
        );
    }

    @Override
    public boolean isValid() {
        return PreferenceStore.getInstance().isValueValid(systolicInputField.getEditText(), Category.PRESSURE)
            && PreferenceStore.getInstance().isValueValid(diastolicInputField.getEditText(), Category.PRESSURE);
    }
}