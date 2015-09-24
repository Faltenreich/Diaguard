package com.faltenreich.diaguard.ui.view;

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
public class MeasurementListView extends LinearLayout {

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

    private boolean hasCategory(Measurement.Category category) {
        return categories.indexOf(category) != -1;
    }

    public void addMeasurement(Measurement.Category category) {
        if (!hasCategory(category)) {
            categories.add(0, category);
            addView(new MeasurementView(getContext(), category), 0);
        }
    }

    public void addMeasurement(Measurement measurement) {
        Measurement.Category category = measurement.getMeasurementType();
        if (!hasCategory(category)) {
            categories.add(0, category);
            addView(new MeasurementView(getContext(), measurement), 0);
        }
    }

    public void removeMeasurement(Measurement.Category category) {
        int position = categories.indexOf(category);
        if (hasCategory(category)) {
            categories.remove(position);
            removeViewAt(position);
        }
    }

    public List<Measurement> getMeasurements() {
        List<Measurement> measurements = new ArrayList<>();
        for (int position = 0; position < getChildCount(); position++) {
            View childView = getChildAt(position);
            if (childView instanceof MeasurementAbstractView) {
                measurements.add(((MeasurementAbstractView) childView).getMeasurement());
            }
        }
        return measurements;
    }

}
