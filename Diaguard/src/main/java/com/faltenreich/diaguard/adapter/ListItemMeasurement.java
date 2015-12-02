package com.faltenreich.diaguard.adapter;

import com.faltenreich.diaguard.data.entity.Measurement;

import org.joda.time.DateTime;

/**
 * Created by Filip on 10.07.2015.
 */
public class ListItemMeasurement extends ListItem {

    private Measurement measurement;

    public ListItemMeasurement(Measurement measurement) {
        super();
        this.measurement = measurement;
    }

    public Measurement getMeasurement() {
        return measurement;
    }

    @Override
    public DateTime getDateTime() {
        return measurement.getEntry().getDate();
    }
}
