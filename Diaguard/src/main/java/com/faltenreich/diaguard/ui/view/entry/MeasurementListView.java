package com.faltenreich.diaguard.ui.view.entry;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.ui.activity.EntryActivity;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class MeasurementListView extends LinearLayout implements MeasurementView.OnCategoryRemovedListener {

    private ArrayList<Measurement.Category> categories;
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

    private void init() {
        this.categories = new ArrayList<>();
    }

    private void addMeasurementView(View view, int position) {
        addView(view, position);
    }

    public boolean hasCategory(Measurement.Category category) {
        return categories.indexOf(category) != -1;
    }

    public void addMeasurement(Measurement.Category category) {
        if (!hasCategory(category)) {
            categories.add(0, category);
            MeasurementView measurementView = new MeasurementView(getContext(), category);
            measurementView.setOnCategoryRemovedListener(this);
            addMeasurementView(measurementView, 0);
            if (callback != null) {
                callback.onCategoryAdded(category);
            }
        }
    }

    public void addMeasurementAtEnd(Measurement.Category category) {
        if (!hasCategory(category)) {
            try {
                int position = categories.size();
                categories.add(position, category);
                MeasurementView measurementView = new MeasurementView(getContext(), category);
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

    public void addMeasurement(Measurement measurement) {
        addMeasurement(0, measurement);
    }

    public void addMeasurement(int index, Measurement measurement) {
        Measurement.Category category = measurement.getCategory();
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
        Measurement.Category category = Measurement.Category.MEAL;
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

    // TODO: Improved performance
    public void addMeasurements(List<Measurement> measurements) {
        long start = DateTime.now().getMillis();

        for (Measurement measurement : measurements) {
            addMeasurement(categories.size(), measurement);
        }

        long end = DateTime.now().getMillis();
        Log.d(EntryActivity.class.getSimpleName(), String.format("Took millis: %d", end - start));
    }

    public void removeMeasurement(Measurement.Category category) {
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
                measurements.add(((MeasurementView) childView).getMeasurement());
            }
        }
        return measurements;
    }

    public Measurement getMeasurement(Measurement.Category category) {
        for (int position = 0; position < getChildCount(); position++) {
            View childView = getChildAt(position);
            if (childView instanceof MeasurementView) {
                Measurement measurement = ((MeasurementView) childView).getMeasurement();
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
    public void onRemove(Measurement.Category category) {
        removeMeasurement(category);
    }

    public interface OnCategoryEventListener {
        void onCategoryAdded(Measurement.Category category);
        void onCategoryRemoved(Measurement.Category category);
    }
}
