package com.faltenreich.diaguard.ui.view.entry;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.util.NumberUtils;

import java.lang.reflect.Constructor;

import butterknife.ButterKnife;

/**
 * Created by Faltenreich on 20.09.2015.
 */
public abstract class MeasurementAbstractView <T extends Measurement> extends LinearLayout {

    private static final String TAG = MeasurementAbstractView.class.getSimpleName();

    protected T measurement;
    protected Food food;

    @Deprecated
    public MeasurementAbstractView(Context context) {
        super(context);
    }

    @Deprecated
    public MeasurementAbstractView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public MeasurementAbstractView(Context context, T measurement) {
        super(context);
        this.measurement = measurement;
        init();
        setValues();
    }

    public MeasurementAbstractView(Context context, Measurement.Category category) {
        super(context);
        try {
            Constructor<T> constructor = category.toClass().getConstructor();
            measurement = constructor.newInstance();
        } catch (Exception exception) {
            Log.e(TAG, String.format("Could not get newInstance for %s", category.toClass().getSimpleName()));
        }
        init();
    }

    public MeasurementAbstractView(Context context, Food food) {
        super(context);
        this.food = food;
        init();
    }

    protected abstract int getLayoutResourceId();

    protected abstract void initLayout();

    protected abstract void setValues();

    protected abstract boolean isValid();

    public abstract Measurement getMeasurement();

    private void init() {
        LayoutInflater.from(getContext()).inflate(getLayoutResourceId(), this);
        ButterKnife.bind(this);
        initLayout();
    }

    protected boolean isValueValid(TextView textView) {
        boolean isValid = true;
        textView.setError(null);
        try {
            float value = PreferenceHelper.getInstance().formatCustomToDefaultUnit(measurement.getCategory(), NumberUtils.parseNumber(textView.getText().toString()));
            if (!PreferenceHelper.getInstance().validateEventValue(measurement.getCategory(), value)) {
                textView.setError(getContext().getString(R.string.validator_value_unrealistic));
                isValid = false;
            }
        } catch (NumberFormatException exception) {
            textView.setError(getContext().getString(R.string.validator_value_number));
            isValid = false;
        }
        return isValid;
    }
}