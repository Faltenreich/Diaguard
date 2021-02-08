package com.faltenreich.diaguard.feature.entry.edit.input;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.viewbinding.ViewBinding;

import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Measurement;
import com.faltenreich.diaguard.shared.data.database.factory.MeasurementFactory;
import com.faltenreich.diaguard.shared.view.ViewBindable;

public abstract class MeasurementInputView<BINDING extends ViewBinding, MEASUREMENT extends Measurement>
    extends LinearLayout
    implements ViewBindable<BINDING>
{

    private BINDING binding;

    private MEASUREMENT measurement;

    @Deprecated
    public MeasurementInputView(Context context) {
        super(context);
    }

    public MeasurementInputView(Context context, MEASUREMENT measurement) {
        super(context);
        this.measurement = measurement;
        this.binding = createBinding(LayoutInflater.from(getContext()));
    }

    public MeasurementInputView(Context context, Category category) {
        this(context, MeasurementFactory.createFromCategory(category));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        onBind(measurement);
    }

    protected abstract BINDING createBinding(LayoutInflater inflater);

    @Override
    public BINDING getBinding() {
        return binding;
    }

    protected abstract void onBind(MEASUREMENT measurement);

    public MEASUREMENT getMeasurement() {
        return measurement;
    }

    public abstract boolean isValid(MEASUREMENT measurement);
}