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

    protected T measurement;

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

    protected abstract int getLayoutResourceId();

    protected abstract void initLayout();

    protected abstract boolean isValid();

    public abstract Measurement getMeasurement();

    private void init() {
        LayoutInflater.from(getContext()).inflate(getLayoutResourceId(), this);
        ButterKnife.bind(this);
        initLayout();
    }
    
}