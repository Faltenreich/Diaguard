package com.faltenreich.diaguard.adapter.list;

/**
 * Created by Faltenreich on 03.09.2016.
 */
public class ListItemNutrient extends ListItem {

    private String label;
    private float value;

    public ListItemNutrient(String label, float value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
