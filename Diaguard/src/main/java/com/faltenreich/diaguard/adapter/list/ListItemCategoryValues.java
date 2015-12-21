package com.faltenreich.diaguard.adapter.list;

import com.faltenreich.diaguard.data.entity.Measurement;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Faltenreich on 15.12.2015.
 */
public class ListItemCategoryValues extends ListItem {

    private Measurement.Category category;
    private float[] values;

    public ListItemCategoryValues(Measurement.Category category, float[] values) {
        this.category = category;
        this.values = values;
    }

    public Measurement.Category getCategory() {
        return category;
    }

    public float[] getValues() {
        return values;
    }

    public float getValue(int index) {
        return values[index];
    }
}
