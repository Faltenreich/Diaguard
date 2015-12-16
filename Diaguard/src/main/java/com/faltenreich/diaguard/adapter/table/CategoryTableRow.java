package com.faltenreich.diaguard.adapter.table;

import com.faltenreich.diaguard.data.entity.Measurement;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Faltenreich on 15.12.2015.
 */
public class CategoryTableRow {

    private Measurement.Category category;
    private String[] values;

    public CategoryTableRow(Measurement.Category category, String[] values) {
        this.category = category;
        this.values = values;
    }

    public Measurement.Category getCategory() {
        return category;
    }

    public String[] getValues() {
        return values;
    }

    public String getValue(int index) {
        return values[index];
    }
}
