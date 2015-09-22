package com.faltenreich.diaguard.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.BloodSugar;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.util.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Faltenreich on 20.09.2015.
 */
public class MeasurementGenericView <T extends Measurement> extends LinearLayout {

    private static final String TAG = MeasurementGenericView.class.getSimpleName();

    @Bind(R.id.edittext_value)
    protected EditText editTextValue;

    private Constructor<T> constructor;
    private Class<T> clazz;

    private T measurement;

    @Deprecated
    public MeasurementGenericView(Context context) {
        super(context);
    }

    public MeasurementGenericView(Context context, Class<T> clazz, T measurement) {
        super(context);
        this.clazz = clazz;
        this.measurement = measurement;
        init();
    }

    public MeasurementGenericView(Context context, Class<T> clazz) {
        super(context);
        this.clazz = clazz;
        init();
    }

    private void init() {
        ButterKnife.bind(this);
        try {
            constructor = clazz.getConstructor();
        } catch (NoSuchMethodException exception) {
            Log.e(TAG, String.format("Could not get constructor for %s", clazz.getSimpleName()));
        }
    }
    
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

    private Measurement getMeasurementInstance() {
        if (measurement == null) {
            try {
                measurement = constructor.newInstance();
            } catch (Exception exception) {
                Log.e(TAG, String.format("Could not get newInstance for %s", clazz.getSimpleName()));
            }
        }
        return measurement;
    }

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
    
}