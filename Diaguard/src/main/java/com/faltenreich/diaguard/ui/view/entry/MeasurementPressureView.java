package com.faltenreich.diaguard.ui.view.entry;

import android.content.Context;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.data.entity.Pressure;
import com.faltenreich.diaguard.ui.view.StickyHintInput;
import com.faltenreich.diaguard.util.NumberUtils;

import butterknife.BindView;

/**
 * Created by Faltenreich on 20.09.2015.
 */
public class MeasurementPressureView extends MeasurementAbstractView<Pressure> {

    @BindView(R.id.pressure_systolic)
    StickyHintInput systolic;
    @BindView(R.id.pressure_diastolic)
    StickyHintInput diastolic;

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

    }

    @Override
    protected void setValues() {
        systolic.setText(measurement.getValuesForUI()[0]);
        diastolic.setText(measurement.getValuesForUI()[1]);
    }

    @Override
    protected boolean isValid() {
        return PreferenceHelper.getInstance().isValueValid(systolic.getInputView(), Measurement.Category.PRESSURE) &&
                PreferenceHelper.getInstance().isValueValid(diastolic.getInputView(), Measurement.Category.PRESSURE);
    }

    @Override
    public Measurement getMeasurement() {
        if (isValid()) {
            measurement.setValues(
                    PreferenceHelper.getInstance().formatCustomToDefaultUnit(
                            measurement.getCategory(),
                            NumberUtils.parseNumber(systolic.getText())),
                    PreferenceHelper.getInstance().formatCustomToDefaultUnit(
                            measurement.getCategory(),
                            NumberUtils.parseNumber(diastolic.getText())));
            return measurement;
        } else {
            return null;
        }
    }
}