package com.faltenreich.diaguard.adapter.list;

import com.faltenreich.diaguard.data.entity.Measurement;

/**
 * Created by Faltenreich on 15.12.2015.
 */
public class ListItemCategoryValue extends ListItemCategory {

    private float value;

    public ListItemCategoryValue(Measurement.Category category, float value) {
        super(category);
        this.value = value;
    }

    public float getValue() {
        return value;
    }
}
