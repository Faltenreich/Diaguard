package com.android.diaguard.adapters;

/**
 * Created by Filip on 18.05.2014.
 */
public class ListItem {

    private String label;
    private String value;
    private boolean isSection;

    public ListItem(String label, String value, boolean isSection) {
        this.label = label;
        this.value = value;
        this.isSection = isSection;
    }

    public ListItem(String key, String value) {
        this.label = key;
        this.value = value;
        this.isSection = false;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isSection() {
        return isSection;
    }
}
