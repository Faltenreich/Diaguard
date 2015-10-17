package com.faltenreich.diaguard.ui.view.entry;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.faltenreich.diaguard.data.entity.Measurement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Faltenreich on 24.09.2015.
 */
public class MeasurementListView extends LinearLayout implements MeasurementView.MeasurementViewCallback {

    private ArrayList<Measurement.Category> categories;

    public MeasurementListView(Context context) {
        super(context);
        init();
    }

    public MeasurementListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    private void init() {
        this.categories = new ArrayList<>();
    }

    public boolean hasCategory(Measurement.Category category) {
        return categories.indexOf(category) != -1;
    }

    public void addMeasurement(Measurement.Category category) {
        if (!hasCategory(category)) {
            categories.add(0, category);
            MeasurementView measurementView = new MeasurementView(getContext(), category);
            measurementView.setMeasurementViewCallback(this);
            addView(measurementView, 0);
        }
    }

    public void addMeasurement(Measurement measurement) {
        if (!hasCategory(measurement.getCategory())) {
            categories.add(0, measurement.getCategory());
            MeasurementView measurementView = new MeasurementView(getContext(), measurement);
            measurementView.setMeasurementViewCallback(this);
            addView(measurementView, 0);
        }
    }

    public void addMeasurements(List<Measurement> measurements) {
        for (Measurement measurement : measurements) {
            addMeasurement(measurement);
        }
    }

    public void removeMeasurement(Measurement.Category category) {
        int position = categories.indexOf(category);
        if (hasCategory(category)) {
            categories.remove(position);
            removeViewAt(position);
        }
    }

    public void removeMeasurement(Measurement measurement) {
        removeMeasurement(measurement.getCategory());
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

    @Override
    public void onCategoryRemoved(Measurement.Category category) {
        removeMeasurement(category);
    }

}
