package com.faltenreich.diaguard.adapter;

import org.joda.time.DateTime;

/**
 * Created by Filip on 10.07.2015.
 */
public abstract class ListItem {

    private int sectionManager;
    private int sectionFirstPosition;
    private boolean isHeader;

    public ListItem(boolean isHeader, int sectionManager, int sectionFirstPosition) {
        this.isHeader = isHeader;
        this.sectionManager = sectionManager;
        this.sectionFirstPosition = sectionFirstPosition;
    }

    public int getSectionManager() {
        return sectionManager;
    }

    public int getSectionFirstPosition() {
        return sectionFirstPosition;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public abstract DateTime getDateTime();
}
