package com.faltenreich.diaguard.feature.entry.edit.input;

import android.content.Context;
import android.view.LayoutInflater;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.ListItemMeasurementGenericBinding;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Measurement;
import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;
import com.faltenreich.diaguard.shared.data.primitive.StringUtils;
import com.faltenreich.diaguard.shared.view.edittext.StickyHintInputView;

/**
 * Created by Faltenreich on 20.09.2015.
 */
public class GenericInputView<T extends Measurement> extends MeasurementInputView<ListItemMeasurementGenericBinding, T> {

    private StickyHintInputView inputField;

    @Deprecated
    public GenericInputView(Context context) {
        super(context);
    }

    public GenericInputView(Context context, T measurement) {
        super(context, measurement);
    }

    public GenericInputView(Context context, Category category) {
        super(context, category);
    }

    @Override
    protected ListItemMeasurementGenericBinding createBinding(LayoutInflater inflater) {
        return ListItemMeasurementGenericBinding.inflate(inflater, this, true);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        inputField = getBinding().inputField;
        inputField.setHint(PreferenceStore.getInstance().getUnitAcronym(measurement.getCategory()));
        inputField.setText(measurement.getValuesForUI()[0]);
    }

    private boolean isValid() {
        boolean isValid;
        String input = inputField.getText();
        if (StringUtils.isBlank(input)) {
            inputField.setError(getContext().getString(R.string.validator_value_empty));
            isValid = false;
        } else {
            isValid = PreferenceStore.getInstance().isValueValid(inputField.getEditText(), measurement.getCategory());
        }
        return isValid;
    }

    @Override
    public Measurement getMeasurement() {
        if (isValid()) {
            float value = FloatUtils.parseNumber(inputField.getText());
            value = PreferenceStore.getInstance().formatCustomToDefaultUnit(measurement.getCategory(), value);
            measurement.setValues(value);
            return measurement;
        } else {
            return null;
        }
    }
    
}