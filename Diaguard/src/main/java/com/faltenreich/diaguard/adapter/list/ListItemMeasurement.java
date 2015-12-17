package com.faltenreich.diaguard.adapter.list;

import com.faltenreich.diaguard.data.entity.Measurement;

/**
 * Created by Filip on 10.07.2015.
 */
public class ListItemMeasurement extends ListItemDate {

    private Measurement measurement;

    public ListItemMeasurement(Measurement measurement) {
        super(measurement.getEntry().getDate());
        this.measurement = measurement;
    }

    public Measurement getMeasurement() {
        return measurement;
    }
}
