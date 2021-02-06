package com.faltenreich.diaguard.feature.entry.edit.measurement;

import android.content.Context;
import android.view.LayoutInflater;

import com.faltenreich.diaguard.databinding.ListItemMeasurementPressureBinding;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Measurement;
import com.faltenreich.diaguard.shared.data.database.entity.Pressure;
import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;
import com.faltenreich.diaguard.shared.view.edittext.StickyHintInputView;

/**
 * Created by Faltenreich on 20.09.2015.
 */
public class MeasurementPressureView extends MeasurementAbstractView<ListItemMeasurementPressureBinding, Pressure> {

    private StickyHintInputView systolicInputField;
    private StickyHintInputView diastolicInputField;

    public MeasurementPressureView(Context context) {
        super(context, Category.PRESSURE);
    }

    public MeasurementPressureView(Context context, Pressure pressure) {
        super(context, pressure);
    }

    @Override
    protected ListItemMeasurementPressureBinding createBinding(LayoutInflater inflater) {
        return ListItemMeasurementPressureBinding.inflate(inflater, this, true);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        systolicInputField = getBinding().systolicInputField;
        systolicInputField.setText(measurement.getValuesForUI()[0]);

        diastolicInputField = getBinding().diastolicInputField;
        diastolicInputField.setText(measurement.getValuesForUI()[1]);
    }

    @Override
    protected boolean isValid() {
        return PreferenceStore.getInstance().isValueValid(systolicInputField.getEditText(), Category.PRESSURE) &&
                PreferenceStore.getInstance().isValueValid(diastolicInputField.getEditText(), Category.PRESSURE);
    }

    @Override
    public Measurement getMeasurement() {
        if (isValid()) {
            measurement.setValues(
                    PreferenceStore.getInstance().formatCustomToDefaultUnit(
                            measurement.getCategory(),
                            FloatUtils.parseNumber(systolicInputField.getText())),
                    PreferenceStore.getInstance().formatCustomToDefaultUnit(
                            measurement.getCategory(),
                            FloatUtils.parseNumber(diastolicInputField.getText())));
            return measurement;
        } else {
            return null;
        }
    }
}