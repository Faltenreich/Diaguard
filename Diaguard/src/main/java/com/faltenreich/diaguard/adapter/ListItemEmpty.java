package com.faltenreich.diaguard.adapter;

import org.joda.time.DateTime;

/**
 * Created by Filip on 10.07.2015.
 */
public class ListItemEmpty extends ListItem {

    private DateTime day;

    public ListItemEmpty(DateTime day) {
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
