package com.faltenreich.diaguard.adapter;

import org.joda.time.DateTime;

/**
 * Created by Filip on 10.07.2015.
 */
public class ListItemDay extends ListItem {

    private DateTime day;

    public ListItemDay(DateTime day) {
        super();
        this.day = day;
    }

    public DateTime getDay() {
        return day;
    }

    @Override
    public DateTime getDateTime() {
        return day;
    }
}
