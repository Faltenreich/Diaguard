package com.faltenreich.diaguard.feature.entry.edit.measurement;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.faltenreich.diaguard.databinding.ListItemMeasurementBinding;
import com.faltenreich.diaguard.feature.entry.edit.input.GenericInputView;
import com.faltenreich.diaguard.feature.entry.edit.input.InsulinInputView;
import com.faltenreich.diaguard.feature.entry.edit.input.MealInputView;
import com.faltenreich.diaguard.feature.entry.edit.input.MeasurementInputView;
import com.faltenreich.diaguard.feature.entry.edit.input.PressureInputView;
import com.faltenreich.diaguard.shared.data.database.entity.Insulin;
import com.faltenreich.diaguard.shared.data.database.entity.Meal;
import com.faltenreich.diaguard.shared.data.database.entity.Measurement;
import com.faltenreich.diaguard.shared.data.database.entity.Pressure;
import com.faltenreich.diaguard.shared.view.ViewBindable;

/**
 * Created by Faltenreich on 24.09.2015.
 */
@SuppressLint("ViewConstructor")
public class MeasurementView<T extends Measurement>
    extends FrameLayout
    implements ViewBindable<ListItemMeasurementBinding>
{

    private final ListItemMeasurementBinding binding;
    private final MeasurementInputView<?, ?> inputView;

    public MeasurementView(Context context, @NonNull T measurement) {
        super(context);
        binding = ListItemMeasurementBinding.inflate(LayoutInflater.from(getContext()), this, true);
        switch (measurement.getCategory()) {
            case INSULIN: this.inputView = new InsulinInputView(getContext(), (Insulin) measurement); break;
            case MEAL: this.inputView = new MealInputView(getContext(), (Meal) measurement); break;
            case PRESSURE: this.inputView = new PressureInputView(getContext(), (Pressure) measurement); break;
            default: this.inputView = new GenericInputView<>(getContext(), measurement.getCategory().toClass(), measurement);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getBinding().contentLayout.addView(inputView);
    }

    @Override
    protected void onDetachedFromWindow() {
        getBinding().contentLayout.removeAllViews();
        super.onDetachedFromWindow();
    }

    @Override
    public ListItemMeasurementBinding getBinding() {
        return binding;
    }

    public Measurement getMeasurement() {
        return inputView.getMeasurement();
    }

    public boolean hasInput() {
        return inputView.hasInput();
    }

    public boolean isValid() {
        return inputView.isValid();
    }
}
