package com.faltenreich.diaguard.feature.entry.edit.measurement;

import android.content.Context;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.preference.PreferenceHelper;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Insulin;
import com.faltenreich.diaguard.shared.data.database.entity.Measurement;
import com.faltenreich.diaguard.shared.view.edittext.StickyHintInput;
import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;
import com.faltenreich.diaguard.shared.data.primitive.StringUtils;

import butterknife.BindView;

/**
 * Created by Faltenreich on 20.09.2015.
 */
public class MeasurementInsulinView extends MeasurementAbstractView<Insulin> {

    @BindView(R.id.insulin_bolus) StickyHintInput inputBolus;
    @BindView(R.id.insulin_correction) StickyHintInput inputCorrection;
    @BindView(R.id.insulin_basal) StickyHintInput inputBasal;

    public MeasurementInsulinView(Context context) {
        super(context, Category.INSULIN);
    }

    public MeasurementInsulinView(Context context, Insulin insulin) {
        super(context, insulin);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.list_item_measurement_insulin;
    }

    @Override
    protected void initLayout() {

    }

    @Override
    protected void setValues() {
        inputBolus.setText(measurement.getValuesForUI()[0]);
        inputCorrection.setText(measurement.getValuesForUI()[1]);
        inputBasal.setText(measurement.getValuesForUI()[2]);
    }

    @Override
    protected boolean isValid() {
        boolean isValid = true;

        String bolus = inputBolus.getText().trim();
        String correction = inputCorrection.getText().trim();
        String basal = inputBasal.getText().trim();

        if (StringUtils.isBlank(bolus) && StringUtils.isBlank(correction) && StringUtils.isBlank(basal)) {
            inputBolus.setError(getContext().getString(R.string.validator_value_empty));
            isValid = false;
        } else {
            if (!StringUtils.isBlank(bolus)) {
                isValid = PreferenceHelper.getInstance().isValueValid(inputBolus.getInputView(), Category.INSULIN);
            }
            if (!StringUtils.isBlank(correction)) {
                isValid = PreferenceHelper.getInstance().isValueValid(inputCorrection.getInputView(), Category.INSULIN, true);
            }
            if (!StringUtils.isBlank(basal)) {
                isValid = PreferenceHelper.getInstance().isValueValid(inputBasal.getInputView(), Category.INSULIN);
            }
        }
        return isValid;
    }

    @Override
    public Measurement getMeasurement() {
        if (isValid()) {
            measurement.setValues(
                    inputBolus.getText().length() > 0 ?
                            PreferenceHelper.getInstance().formatCustomToDefaultUnit(
                                    measurement.getCategory(),
                                    FloatUtils.parseNumber(inputBolus.getText())) : 0,
                    inputCorrection.getText().length() > 0 ?
                            PreferenceHelper.getInstance().formatCustomToDefaultUnit(
                                    measurement.getCategory(),
                                    FloatUtils.parseNumber(inputCorrection.getText())) : 0,
                    inputBasal.getText().length() > 0 ?
                            PreferenceHelper.getInstance().formatCustomToDefaultUnit(
                                    measurement.getCategory(),
                                    FloatUtils.parseNumber(inputBasal.getText())) : 0);
            return measurement;
        } else {
            return null;
        }
    }
}