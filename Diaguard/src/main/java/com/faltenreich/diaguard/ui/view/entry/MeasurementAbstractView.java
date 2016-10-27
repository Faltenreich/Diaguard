package com.faltenreich.diaguard.ui.view.entry;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.data.entity.Meal;
import com.faltenreich.diaguard.data.entity.Measurement;

import java.lang.reflect.Constructor;

import butterknife.ButterKnife;

/**
 * Created by Faltenreich on 20.09.2015.
 */
public abstract class MeasurementAbstractView <T extends Measurement> extends LinearLayout implements TextWatcher {

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
        this.measurement = (T) new Meal();
        this.food = food;
        init();
    }

    protected abstract int getLayoutResourceId();

    protected abstract void initLayout();

    protected abstract void setValues();

    protected abstract boolean isValid();

    public abstract Measurement getMeasurement();

    protected InputLabel[] getInputLabels() {
        return new InputLabel[0];
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(getLayoutResourceId(), this);
        ButterKnife.bind(this);
        initLayout();
        initInputLabels();
    }

    private void initInputLabels() {
        for (InputLabel inputLabel : getInputLabels()) {
            inputLabel.getInput().addTextChangedListener(this);
        }
    }

    private void invalidateLabels() {
        for (InputLabel inputLabel : getInputLabels()) {
            toggleLabel(inputLabel.getLabel(), inputLabel.getInput());
        }
    }

    protected void toggleLabel(TextView label, EditText input) {
        int oldVisibility = label.getVisibility();
        boolean isVisible = input.getText().toString().length() > 0;
        int newVisibility = isVisible ? VISIBLE : GONE;
        if (oldVisibility != newVisibility) {
            label.setVisibility(newVisibility);
            label.setAlpha(isVisible ? 0f : 1f);
            label.animate().alpha(isVisible ? 1f : 0f);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        invalidateLabels();
    }
}