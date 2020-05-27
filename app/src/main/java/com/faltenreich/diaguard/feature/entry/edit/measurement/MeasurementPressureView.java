package com.faltenreich.diaguard.feature.entry.edit.measurement;

import android.content.Context;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Measurement;
import com.faltenreich.diaguard.shared.data.database.entity.Pressure;
import com.faltenreich.diaguard.shared.view.edittext.StickyHintInput;
import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;

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
    protected void initLayout() {

    }

    @Override
    protected void setValues() {
        systolic.setText(measurement.getValuesForUI()[0]);
        diastolic.setText(measurement.getValuesForUI()[1]);
    }

    @Override
    protected boolean isValid() {
        return PreferenceStore.getInstance().isValueValid(systolic.getEditText(), Category.PRESSURE) &&
                PreferenceStore.getInstance().isValueValid(diastolic.getEditText(), Category.PRESSURE);
    }

    @Override
    public Measurement getMeasurement() {
        if (isValid()) {
            measurement.setValues(
                    PreferenceStore.getInstance().formatCustomToDefaultUnit(
                            measurement.getCategory(),
                            FloatUtils.parseNumber(systolic.getText())),
                    PreferenceStore.getInstance().formatCustomToDefaultUnit(
                            measurement.getCategory(),
                            FloatUtils.parseNumber(diastolic.getText())));
            return measurement;
        } else {
            return null;
        }
    }
}