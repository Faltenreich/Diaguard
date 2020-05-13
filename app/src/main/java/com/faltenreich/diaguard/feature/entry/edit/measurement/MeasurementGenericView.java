package com.faltenreich.diaguard.feature.entry.edit.measurement;

import android.content.Context;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.shared.data.preference.PreferenceHelper;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Measurement;
import com.faltenreich.diaguard.shared.view.edittext.StickyHintInput;
import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;
import com.faltenreich.diaguard.shared.data.primitive.StringUtils;

import butterknife.BindView;

/**
 * Created by Faltenreich on 20.09.2015.
 */
public class MeasurementGenericView <T extends Measurement> extends MeasurementAbstractView<T> {

    @BindView(R.id.value) StickyHintInput inputView;

    @Deprecated
    public MeasurementGenericView(Context context) {
        super(context);
    }

    public MeasurementGenericView(Context context, T measurement) {
        super(context, measurement);
    }

    public MeasurementGenericView(Context context, Category category) {
        super(context, category);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.list_item_measurement_generic;
    }

    @Override
    protected void initLayout() {
        inputView.setHint(PreferenceHelper.getInstance().getUnitAcronym(measurement.getCategory()));
    }

    @Override
    protected void setValues() {
        inputView.setText(measurement.getValuesForUI()[0]);
    }

    @Override
    protected boolean isValid() {
        boolean isValid;
        String input = inputView.getText();
        if (StringUtils.isBlank(input)) {
            inputView.setError(getContext().getString(R.string.validator_value_empty));
            isValid = false;
        } else {
            isValid = PreferenceHelper.getInstance().isValueValid(inputView.getInputView(), measurement.getCategory());
        }
        return isValid;
    }

    @Override
    public Measurement getMeasurement() {
        if (isValid()) {
            float value = FloatUtils.parseNumber(inputView.getText());
            value = PreferenceHelper.getInstance().formatCustomToDefaultUnit(measurement.getCategory(), value);
            measurement.setValues(value);
            return measurement;
        } else {
            return null;
        }
    }
    
}