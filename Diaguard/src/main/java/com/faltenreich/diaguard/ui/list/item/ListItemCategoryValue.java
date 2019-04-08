package com.faltenreich.diaguard.ui.list.item;

import com.faltenreich.diaguard.data.entity.Measurement;

/**
 * Created by Faltenreich on 15.12.2015.
 */
public class ListItemCategoryValue extends ListItemCategory {

    private float valueOne;
    private float valueTwo;
    private float valueThree;

    public ListItemCategoryValue(Measurement.Category category) {
        super(category);
    }

    public ListItemCategoryValue(Measurement.Category category, float valueOne, float valueTwo, float valueThree) {
        super(category);
        this.valueOne = valueOne;
        this.valueTwo = valueTwo;
        this.valueThree = valueThree;
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

    public float getValueThree() {
        return valueThree;
    }

    public void setValueThree(float valueThree) {
        this.valueThree = valueThree;
    }

    public float getValueTotal() {
        return valueOne + valueTwo + valueThree;
    }
}
