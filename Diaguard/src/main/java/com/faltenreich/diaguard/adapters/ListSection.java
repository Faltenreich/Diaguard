package com.faltenreich.diaguard.adapters;

/**
 * Created by Filip on 20.08.2014.
 */
public class ListSection implements ListItem {

    private final String sectionName;

    public ListSection(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getSectionName() {
        return this.sectionName;
    }

    @Override
    public boolean isSection() {
        return true;
    }
}
