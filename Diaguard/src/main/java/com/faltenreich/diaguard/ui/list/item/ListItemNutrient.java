package com.faltenreich.diaguard.ui.list.item;

/**
 * Created by Faltenreich on 03.09.2016.
 */
public class ListItemNutrient extends ListItem {

    private String label;
    private String value;

    public ListItemNutrient(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
