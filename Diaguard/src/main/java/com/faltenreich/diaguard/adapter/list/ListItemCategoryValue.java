package com.faltenreich.diaguard.adapter.list;

import com.faltenreich.diaguard.data.entity.Measurement;

/**
 * Created by Faltenreich on 15.12.2015.
 */
public class ListItemCategoryValue extends ListItemCategory {

    private float valueOne;
    private float valueTwo;

    public ListItemCategoryValue(Measurement.Category category) {
        super(category);
    }

    public ListItemCategoryValue(Measurement.Category category, float valueOne, float valueTwo) {
        super(category);
        this.valueOne = valueOne;
        this.valueTwo = valueTwo;
    }

    public ListItemCategoryValue(Measurement.Category category, float valueOne) {
        this(category, valueOne, 0);
    }

    public float getValueOne() {
        return valueOne;
    }

    public void setValueOne(float valueOne) {
        this.valueOne = valueOne;
    }

    public float getValueTwo() {
        return valueTwo;
    }

    public void setValueTwo(float valueTwo) {
        this.valueTwo = valueTwo;
    }
}
