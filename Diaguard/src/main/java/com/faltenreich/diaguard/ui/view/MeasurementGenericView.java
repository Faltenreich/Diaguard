package com.faltenreich.diaguard.ui.view;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.util.StringUtils;

import java.lang.reflect.Constructor;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Faltenreich on 20.09.2015.
 */
public class MeasurementGenericView <T extends Measurement> extends MeasurementAbstractView<T> {

    private static final String TAG = MeasurementGenericView.class.getSimpleName();

    @Bind(R.id.edittext_value)
    protected EditText editTextValue;

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
    public Measurement getMeasurement() {
        try {
            Measurement measurement = getMeasurementInstance();
            measurement.setValues(Float.parseFloat(editTextValue.getText().toString()));
            return measurement;
        } catch (NumberFormatException exception) {
            Log.e(TAG, exception.getMessage());
            return null;
        }
    }

    @Override
    public boolean isValid() {
        boolean isValid = true;
        String input = editTextValue.getText().toString();
        if (StringUtils.isBlank(input)) {
            editTextValue.setError(getContext().getString(R.string.validator_value_empty));
            isValid = false;
        } else {
            try {
                float value = Float.parseFloat(input);
                if (!PreferenceHelper.getInstance().validateEventValue(getMeasurementInstance().getMeasurementType(), value)) {
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
    
}