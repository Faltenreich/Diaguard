package com.faltenreich.diaguard.feature.entry.edit.measurement;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.viewbinding.ViewBinding;

import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.data.database.entity.Meal;
import com.faltenreich.diaguard.shared.data.database.entity.Measurement;
import com.faltenreich.diaguard.shared.view.ViewBindable;

import java.lang.reflect.Constructor;

/**
 * Created by Faltenreich on 20.09.2015.
 */
public abstract class MeasurementAbstractView <BINDING extends ViewBinding, MEASUREMENT extends Measurement>
    extends LinearLayout implements ViewBindable<BINDING> {

    private static final String TAG = MeasurementAbstractView.class.getSimpleName();

    private BINDING binding;

    protected MEASUREMENT measurement;
    protected Food food;

    @Deprecated
    public MeasurementAbstractView(Context context) {
        super(context);
        init();
    }

    @Deprecated
    public MeasurementAbstractView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public MeasurementAbstractView(Context context, MEASUREMENT measurement) {
        super(context);
        this.measurement = measurement;
        init();
    }

    public MeasurementAbstractView(Context context, Food food) {
        super(context);
        this.measurement = (MEASUREMENT) new Meal();
        this.food = food;
        init();
    }

    public MeasurementAbstractView(Context context, Category category) {
        super(context);
        try {
            Class<MEASUREMENT> clazz = category.toClass();
            Constructor<MEASUREMENT> constructor = clazz.getConstructor();
            measurement = constructor.newInstance();
        } catch (Exception exception) {
            Log.e(TAG, String.format("Could not get newInstance for %s", category.toClass().getSimpleName()));
        }
        init();
    }

    protected abstract BINDING createBinding(View view);

    @Override
    public BINDING getBinding() {
        return binding;
    }

    protected abstract int getLayoutResourceId();

    protected abstract void initLayout();

    protected abstract void setValues();

    protected abstract boolean isValid();

    public abstract Measurement getMeasurement();

    private void init() {
        LayoutInflater.from(getContext()).inflate(getLayoutResourceId(), this);
        binding = createBinding(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        initLayout();
        if (measurement != null) {
            setValues();
        }
    }
}