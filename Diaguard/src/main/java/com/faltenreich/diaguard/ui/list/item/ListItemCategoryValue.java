package com.faltenreich.diaguard.ui.list.item;

import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.util.Helper;

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

    public String print() {
        StringBuilder stringBuilder = new StringBuilder();
        if (getCategory().stackValues()) {
            float value = getValueTotal();
            if (value > 0) {
                value = PreferenceHelper.getInstance().formatDefaultToCustomUnit(getCategory(), getValueTotal());
                stringBuilder.append(Helper.parseFloat(value));
            }
        } else {
            if (getValueOne() > 0) {
                float value = PreferenceHelper.getInstance().formatDefaultToCustomUnit(getCategory(), getValueOne());
                stringBuilder.append(Helper.parseFloat(value));
            }
            if (getValueTwo() > 0) {
                if (stringBuilder.length() > 0) {
                    stringBuilder.append("\n");
                }
                float value = PreferenceHelper.getInstance().formatDefaultToCustomUnit(getCategory(), getValueTwo());
                stringBuilder.append(Helper.parseFloat(value));
            }
            if (getValueThree() > 0) {
                if (stringBuilder.length() > 0) {
                    stringBuilder.append("\n");
                }
                float value = PreferenceHelper.getInstance().formatDefaultToCustomUnit(getCategory(), getValueThree());
                stringBuilder.append(Helper.parseFloat(value));
            }
        }
        return stringBuilder.toString();
    }
}
