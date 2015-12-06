package com.faltenreich.diaguard.adapter.list;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 * Created by Filip on 10.07.2015.
 */
public abstract class ListItem {

    private DateTime dateTime;

    public ListItem(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    @Override
    public String toString() {
        return DateTimeFormat.mediumDate().print(dateTime);
    }
}
