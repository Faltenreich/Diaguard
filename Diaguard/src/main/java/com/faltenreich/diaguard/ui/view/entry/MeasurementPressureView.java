package com.faltenreich.diaguard.ui.view.entry;

import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.data.entity.Pressure;
import com.faltenreich.diaguard.util.NumberUtils;

import butterknife.BindView;

/**
 * Created by Faltenreich on 20.09.2015.
 */
public class MeasurementPressureView extends MeasurementAbstractView<Pressure> {

    @BindView(R.id.edittext_systolic) EditText editTextSystolic;
    @BindView(R.id.edittext_diastolic) EditText editTextDiastolic;

    @BindView(R.id.pressure_systolic_label) TextView systolicLabel;
    @BindView(R.id.pressure_diastolic_label) TextView diastolicLabel;

    public MeasurementPressureView(Context context) {
        super(context, Measurement.Category.PRESSURE);
    }

    public MeasurementPressureView(Context context, Pressure pressure) {
        super(context, pressure);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.list_item_measurement_pressure;
    }

    @Override
    protected void initLayout() {
        editTextSystolic.setHint(getContext().getString(R.string.systolic));
        editTextDiastolic.setHint(getContext().getString(R.string.diastolic));
    }

    @Override
    protected void setValues() {
        editTextSystolic.setText(measurement.getValuesForUI()[0]);
        editTextDiastolic.setText(measurement.getValuesForUI()[1]);
    }

    @Override
    protected boolean isValid() {
        return PreferenceHelper.isValueValid(editTextSystolic, Measurement.Category.PRESSURE) &&
                PreferenceHelper.isValueValid(editTextDiastolic, Measurement.Category.PRESSURE);
    }

    @Override
    public Measurement getMeasurement() {
        if (isValid()) {
            measurement.setValues(
                    PreferenceHelper.getInstance().formatCustomToDefaultUnit(
                            measurement.getCategory(),
                            NumberUtils.parseNumber(editTextSystolic.getText().toString())),
                    PreferenceHelper.getInstance().formatCustomToDefaultUnit(
                            measurement.getCategory(),
                            NumberUtils.parseNumber(editTextDiastolic.getText().toString())));
            return measurement;
        } else {
            return null;
        }
    }

    @Override
    protected InputLabel[] getInputLabels() {
        return new InputLabel[] {
                new InputLabel(editTextSystolic, systolicLabel),
                new InputLabel(editTextDiastolic, diastolicLabel)
        };
    }
}