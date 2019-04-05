package com.faltenreich.diaguard.ui.list.item;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 * Created by Filip on 10.07.2015.
 */
public abstract class ListItemDate extends ListItem {

    private DateTime dateTime;

    public ListItemDate(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    @Override
    public String toString() {
        return DateTimeFormat.mediumDateTime().print(dateTime);
    }
}
