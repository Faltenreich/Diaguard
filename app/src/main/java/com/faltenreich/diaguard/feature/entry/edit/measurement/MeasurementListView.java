package com.faltenreich.diaguard.feature.entry.edit.measurement;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.data.database.entity.Measurement;

import java.util.ArrayList;
import java.util.List;

public class MeasurementListView extends LinearLayout implements MeasurementView.OnCategoryRemovedListener {

    private ArrayList<Category> categories;
    private OnCategoryEventListener callback;

    public MeasurementListView(Context context) {
        super(context);
        init();
    }

    public MeasurementListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public void setOnCategoryEventListener(OnCategoryEventListener callback) {
        this.callback = callback;
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable savedState = super.onSaveInstanceState();
        // FIXME: Avoid casting to harden api
        List<Measurement> measurements = getMeasurements();
        return new MeasurementSavedState(savedState, (ArrayList<Measurement>) measurements);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        if (state instanceof MeasurementSavedState) {
            MeasurementSavedState measurementSavedState = (MeasurementSavedState) state;
            // FIXME: Retrieve correctly cached measurements
            List<Measurement> measurements = measurementSavedState.getMeasurements();
            Log.d(MeasurementListView.class.getSimpleName(), "Restored measurements: " + measurements.toString());
            for (int index = 0; index < measurements.size(); index++) {
                Measurement measurement = measurements.get(index);
                // TODO: Add measurements when views are prepared
                // addMeasurement(index, measurement);
            }
        }
    }

    private void init() {
        this.categories = new ArrayList<>();
    }

    private void addMeasurementView(View view, int position) {
        addView(view, position);
    }

    public boolean hasCategory(Category category) {
        return categories.contains(category);
    }

    public void addMeasurement(Category category) {
        if (!hasCategory(category)) {
            categories.add(0, category);
            MeasurementView<?> measurementView = new MeasurementView<>(getContext(), category);
            measurementView.setOnCategoryRemovedListener(this);
            addMeasurementView(measurementView, 0);
            if (callback != null) {
                callback.onCategoryAdded(category);
            }
        }
    }

    public void addMeasurementAtEnd(Category category) {
        if (!hasCategory(category)) {
            try {
                int position = categories.size();
                categories.add(position, category);
                MeasurementView<?> measurementView = new MeasurementView<>(getContext(), category);
                measurementView.setOnCategoryRemovedListener(this);
                addMeasurementView(measurementView, position);
                if (callback != null) {
                    callback.onCategoryAdded(category);
                }
            } catch (IndexOutOfBoundsException exception) {
                Log.e(MeasurementListView.class.getSimpleName(), exception.getMessage());
            }
        }
    }

    public void addMeasurement(int index, Measurement measurement) {
        Category category = measurement.getCategory();
        if (!hasCategory(category)) {
            categories.add(index, category);
            MeasurementView<Measurement> measurementView = new MeasurementView<>(getContext(), measurement);
            measurementView.setOnCategoryRemovedListener(this);
            addMeasurementView(measurementView, index);
            if (callback != null) {
                callback.onCategoryAdded(category);
            }
        }
    }

    public void addMeasurement(Food food) {
        Category category = Category.MEAL;
        if (!hasCategory(category)) {
            categories.add(0, category);
            MeasurementView<Measurement> measurementView = new MeasurementView<>(getContext(), food);
            measurementView.setOnCategoryRemovedListener(this);
            addMeasurementView(measurementView, 0);
            if (callback != null) {
                callback.onCategoryAdded(category);
            }
        }
    }

    public void addMeasurements(List<Measurement> measurements) {
        for (Measurement measurement : measurements) {
            addMeasurement(categories.size(), measurement);
        }
    }

    public void removeMeasurement(Category category) {
        int position = categories.indexOf(category);
        if (hasCategory(category)) {
            categories.remove(position);
            removeViewAt(position);
            if (callback != null) {
                callback.onCategoryRemoved(category);
            }
        }
    }

    public List<Measurement> getMeasurements() {
        List<Measurement> measurements = new ArrayList<>();
        for (int position = 0; position < getChildCount(); position++) {
            View childView = getChildAt(position);
            if (childView instanceof MeasurementView) {
                measurements.add(((MeasurementView<?>) childView).getMeasurement());
            }
        }
        return measurements;
    }

    public Measurement getMeasurement(Category category) {
        for (int position = 0; position < getChildCount(); position++) {
            View childView = getChildAt(position);
            if (childView instanceof MeasurementView) {
                Measurement measurement = ((MeasurementView<?>) childView).getMeasurement();
                if (measurement.getCategory() == category) {
                    return measurement;
                }
            }
        }
        return null;
    }

    public int getCount() {
        return categories.size();
    }

    @Override
    public void onRemove(Category category) {
        removeMeasurement(category);
    }

    public interface OnCategoryEventListener {
        void onCategoryAdded(Category category);
        void onCategoryRemoved(Category category);
    }
}
