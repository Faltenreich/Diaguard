package com.faltenreich.diaguard.ui.view;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.BloodSugar;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.util.StringUtils;

import java.lang.reflect.Constructor;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Faltenreich on 20.09.2015.
 */
public abstract class MeasurementAbstractView <T extends Measurement> extends LinearLayout {

    private static final String TAG = MeasurementAbstractView.class.getSimpleName();

    private Constructor<T> constructor;
    private Class<T> clazz;

    private T measurement;

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
        this.clazz = measurement.getMeasurementType().toClass();
        this.measurement = measurement;
        init();
    }

    public MeasurementAbstractView(Context context, Measurement.Category category) {
        super(context);
        this.clazz = category.toClass();
        init();
    }

    protected abstract int getLayoutResourceId();

    public abstract Measurement getMeasurement();

    public abstract boolean isValid();

    private void init() {
        LayoutInflater.from(getContext()).inflate(getLayoutResourceId(), this);
        ButterKnife.bind(this);
        try {
            constructor = clazz.getConstructor();
        } catch (NoSuchMethodException exception) {
            Log.e(TAG, String.format("Could not get constructor for %s", clazz.getSimpleName()));
        }
    }

    protected Measurement getMeasurementInstance() {
        if (measurement == null) {
            try {
                measurement = constructor.newInstance();
            } catch (Exception exception) {
                Log.e(TAG, String.format("Could not get newInstance for %s", clazz.getSimpleName()));
            }
        }
        return measurement;
    }
    
}