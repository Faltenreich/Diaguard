package com.faltenreich.diaguard.feature.entry.edit.measurement;

import android.content.Context;
import android.view.View;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.ListItemMeasurementPressureBinding;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Measurement;
import com.faltenreich.diaguard.shared.data.database.entity.Pressure;
import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;
import com.faltenreich.diaguard.shared.view.edittext.StickyHintInput;

/**
 * Created by Faltenreich on 20.09.2015.
 */
public class MeasurementPressureView extends MeasurementAbstractView<ListItemMeasurementPressureBinding, Pressure> {

    private StickyHintInput systolicInputField;
    private StickyHintInput diastolicInputField;

    public MeasurementPressureView(Context context) {
        super(context, Category.PRESSURE);
    }

    public MeasurementPressureView(Context context, Pressure pressure) {
        super(context, pressure);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.list_item_measurement_pressure;
    }

    @Override
    protected ListItemMeasurementPressureBinding createBinding(View view) {
        return ListItemMeasurementPressureBinding.bind(view);
    }

    @Override
    protected void initLayout() {
        systolicInputField = getBinding().systolicInputField;
        diastolicInputField = getBinding().diastolicInputField;
    }

    @Override
    protected void setValues() {
        systolicInputField.setText(measurement.getValuesForUI()[0]);
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