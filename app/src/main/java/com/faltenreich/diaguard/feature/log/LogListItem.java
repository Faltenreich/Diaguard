package com.faltenreich.diaguard.feature.log;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 * Created by Filip on 10.07.2015.
 */
public abstract class LogListItem {

    private final DateTime dateTime;

    public LogListItem(DateTime dateTime) {
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
