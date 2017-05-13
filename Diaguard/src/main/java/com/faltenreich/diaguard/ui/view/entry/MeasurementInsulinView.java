package com.faltenreich.diaguard.ui.view.entry;

import android.content.Context;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Insulin;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.ui.view.StickyHintInput;
import com.faltenreich.diaguard.util.NumberUtils;
import com.faltenreich.diaguard.util.StringUtils;

import butterknife.BindView;

/**
 * Created by Faltenreich on 20.09.2015.
 */
public class MeasurementInsulinView extends MeasurementAbstractView<Insulin> {

    @BindView(R.id.insulin_bolus)
    StickyHintInput inputBolus;
    @BindView(R.id.insulin_correction)
    StickyHintInput inputCorrection;
    @BindView(R.id.insulin_basal)
    StickyHintInput inputBasal;

    public MeasurementInsulinView(Context context) {
        super(context, Measurement.Category.INSULIN);
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
                isValid = PreferenceHelper.isValueValid(inputBolus.getEditText(), Measurement.Category.INSULIN);
            }
            if (!StringUtils.isBlank(basal)) {
                isValid = PreferenceHelper.isValueValid(inputBasal.getEditText(), Measurement.Category.INSULIN);
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
                                    NumberUtils.parseNumber(inputBolus.getText())) : 0,
                    inputCorrection.getText().length() > 0 ?
                            PreferenceHelper.getInstance().formatCustomToDefaultUnit(
                                    measurement.getCategory(),
                                    NumberUtils.parseNumber(inputCorrection.getText())) : 0,
                    inputBasal.getText().length() > 0 ?
                            PreferenceHelper.getInstance().formatCustomToDefaultUnit(
                                    measurement.getCategory(),
                                    NumberUtils.parseNumber(inputBasal.getText())) : 0);
            return measurement;
        } else {
            return null;
        }
    }
}