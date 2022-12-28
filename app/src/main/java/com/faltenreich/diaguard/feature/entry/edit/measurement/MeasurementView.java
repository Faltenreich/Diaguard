package com.faltenreich.diaguard.feature.entry.edit.measurement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.faltenreich.diaguard.databinding.ListItemMeasurementBinding;
import com.faltenreich.diaguard.feature.entry.edit.input.GenericInputView;
import com.faltenreich.diaguard.feature.entry.edit.input.InsulinInputView;
import com.faltenreich.diaguard.feature.entry.edit.input.MealInputView;
import com.faltenreich.diaguard.feature.entry.edit.input.MeasurementInputView;
import com.faltenreich.diaguard.feature.entry.edit.input.PressureInputView;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Insulin;
import com.faltenreich.diaguard.shared.data.database.entity.Meal;
import com.faltenreich.diaguard.shared.data.database.entity.Measurement;
import com.faltenreich.diaguard.shared.data.database.entity.Pressure;
import com.faltenreich.diaguard.shared.view.ViewBindable;

/**
 * Created by Faltenreich on 24.09.2015.
 */
public class MeasurementView<T extends Measurement> extends FrameLayout implements ViewBindable<ListItemMeasurementBinding> {

    private ViewGroup contentLayout;

    private T measurement;
    private MeasurementInputView<?, T> inputView;

    private ListItemMeasurementBinding binding;

    @Deprecated
    public MeasurementView(Context context) {
        super(context);
    }

    public MeasurementView(Context context, @NonNull T measurement) {
        super(context);
        this.measurement = measurement;
        init();
    }

    @Override
    public ListItemMeasurementBinding getBinding() {
        return binding;
    }

    public MeasurementInputView<?, T> getInputView() {
        return inputView;
    }

    private void init() {
        binding = ListItemMeasurementBinding.inflate(LayoutInflater.from(getContext()), this, true);

        View inputView;
        Category category = measurement.getCategory();
        switch (category) {
            case INSULIN:
                inputView = new InsulinInputView(getContext(), (Insulin) measurement);
                break;
            case MEAL:
                inputView = new MealInputView(getContext(), (Meal) measurement);
                break;
            case PRESSURE:
                inputView = new PressureInputView(getContext(), (Pressure) measurement);
                break;
            default:
                inputView = new GenericInputView<>(getContext(), category.toClass(), measurement);
        }
        //noinspection unchecked
        this.inputView = (MeasurementInputView<?, T>) inputView;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        bindViews();
        initLayout();
    }

    @Override
    protected void onDetachedFromWindow() {
        contentLayout.removeAllViews();
        super.onDetachedFromWindow();
    }

    private void bindViews() {
        contentLayout = getBinding().contentLayout;
    }

    private void initLayout() {
        contentLayout.addView(inputView);
    }

    public Measurement getMeasurement() {
        return measurement;
    }
}
