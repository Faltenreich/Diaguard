package com.faltenreich.diaguard.feature.entry.edit.measurement;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.viewbinding.ViewBinding;

import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Measurement;
import com.faltenreich.diaguard.shared.data.database.factory.MeasurementFactory;
import com.faltenreich.diaguard.shared.view.ViewBindable;

public abstract class MeasurementAbstractView<BINDING extends ViewBinding, MEASUREMENT extends Measurement>
    extends LinearLayout
    implements ViewBindable<BINDING>
{

    private BINDING binding;

    protected MEASUREMENT measurement;

    @Deprecated
    public MeasurementAbstractView(Context context) {
        super(context);
    }

    public MeasurementAbstractView(Context context, MEASUREMENT measurement) {
        super(context);
        this.measurement = measurement;
        this.binding = createBinding(LayoutInflater.from(getContext()));
    }

    public MeasurementAbstractView(Context context, Category category) {
        this(context, MeasurementFactory.createFromCategory(category));
    }

    protected abstract BINDING createBinding(LayoutInflater inflater);

    @Override
    public BINDING getBinding() {
        return binding;
    }

    public abstract Measurement getMeasurement();
}