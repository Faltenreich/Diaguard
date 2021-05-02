package com.faltenreich.diaguard.feature.entry.edit.input;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.viewbinding.ViewBinding;

import com.faltenreich.diaguard.shared.data.database.entity.Measurement;
import com.faltenreich.diaguard.shared.data.reflect.ObjectFactory;
import com.faltenreich.diaguard.shared.view.ViewBindable;

public abstract class MeasurementInputView<BINDING extends ViewBinding, MEASUREMENT extends Measurement>
    extends LinearLayout
    implements ViewBindable<BINDING>
{

    private final BINDING binding;
    private MEASUREMENT measurement;

    public MeasurementInputView(Context context) {
        super(context);
        this.binding = createBinding(LayoutInflater.from(getContext()));
    }

    public MeasurementInputView(Context context, Class<MEASUREMENT> clazz, MEASUREMENT measurement) {
        this(context);
        this.measurement = measurement != null ? measurement : ObjectFactory.createFromClass(clazz);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        onBind(measurement);
    }

    @Override
    protected void onDetachedFromWindow() {
        onUnbind(measurement);
        super.onDetachedFromWindow();
    }

    protected abstract BINDING createBinding(LayoutInflater inflater);

    @Override
    public BINDING getBinding() {
        return binding;
    }

    protected abstract void onBind(MEASUREMENT measurement);

    protected void onUnbind(MEASUREMENT measurement) {}

    public MEASUREMENT getMeasurement() {
        return measurement;
    }

    public abstract boolean isValid();
}