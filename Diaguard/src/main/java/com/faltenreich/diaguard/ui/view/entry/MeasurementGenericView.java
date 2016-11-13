package com.faltenreich.diaguard.ui.view.entry;

import android.content.Context;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.ui.view.StickyHintInput;
import com.faltenreich.diaguard.util.NumberUtils;
import com.faltenreich.diaguard.util.StringUtils;

import butterknife.BindView;

/**
 * Created by Faltenreich on 20.09.2015.
 */
public class MeasurementGenericView <T extends Measurement> extends MeasurementAbstractView<T> {

    @BindView(R.id.value)
    StickyHintInput value;

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
        value.setHint(PreferenceHelper.getInstance().getUnitAcronym(measurement.getCategory()));
    }

    @Override
    protected void setValues() {
        value.setText(measurement.getValuesForUI()[0]);
    }

    @Override
    protected boolean isValid() {
        boolean isValid;
        String input = value.getText();
        if (StringUtils.isBlank(input)) {
            value.setError(getContext().getString(R.string.validator_value_empty));
            isValid = false;
        } else {
            isValid = PreferenceHelper.isValueValid(value.getEditText(), measurement.getCategory());
        }
        return isValid;
    }

    @Override
    public Measurement getMeasurement() {
        if (isValid()) {
            measurement.setValues(
                    PreferenceHelper.getInstance().formatCustomToDefaultUnit(
                            measurement.getCategory(),
                            NumberUtils.parseNumber(value.getText())));
            return measurement;
        } else {
            return null;
        }
    }
    
}