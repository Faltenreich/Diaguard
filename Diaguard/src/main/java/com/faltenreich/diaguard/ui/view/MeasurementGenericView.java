package com.faltenreich.diaguard.ui.view;

import android.content.Context;
import android.widget.EditText;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.util.StringUtils;

import butterknife.Bind;

/**
 * Created by Faltenreich on 20.09.2015.
 */
public class MeasurementGenericView <T extends Measurement> extends MeasurementAbstractView<T> {

    @Bind(R.id.edittext_value)
    protected EditText editTextValue;

    @Deprecated
    public MeasurementGenericView(Context context) {
        super(context);
    }

    public MeasurementGenericView(Context context, T measurement) {
        super(context, measurement);
    }

    public MeasurementGenericView(Context context, Measurement.Category category) {
        super(context, category);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.list_item_measurement_generic;
    }

    @Override
    protected void initLayout() {
        editTextValue.setHint(PreferenceHelper.getInstance().getUnitAcronym(measurement.getCategory()));
    }

    @Override
    protected void setValues() {
        editTextValue.setText(measurement.getValuesForUI()[0]);
    }

    @Override
    protected boolean isValid() {
        boolean isValid = true;
        String input = editTextValue.getText().toString();
        if (StringUtils.isBlank(input)) {
            editTextValue.setError(getContext().getString(R.string.validator_value_empty));
            isValid = false;
        } else {
            try {
                float value = PreferenceHelper.getInstance().formatCustomToDefaultUnit(measurement.getCategory(), Float.parseFloat(input));
                if (!PreferenceHelper.getInstance().validateEventValue(measurement.getCategory(), value)) {
                    editTextValue.setError(getContext().getString(R.string.validator_value_unrealistic));
                    isValid = false;
                }
            } catch (NumberFormatException exception) {
                editTextValue.setError(getContext().getString(R.string.validator_value_number));
                isValid = false;
            }
        }
        return isValid;
    }

    @Override
    public Measurement getMeasurement() {
        if (isValid()) {
            measurement.setValues(
                    PreferenceHelper.getInstance().formatCustomToDefaultUnit(
                            measurement.getCategory(),
                            Float.parseFloat(editTextValue.getText().toString())));
            return measurement;
        } else {
            return null;
        }
    }
    
}