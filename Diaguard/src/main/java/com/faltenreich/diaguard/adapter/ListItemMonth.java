package com.faltenreich.diaguard.adapter;

import org.joda.time.DateTime;

/**
 * Created by Filip on 10.07.2015.
 */
public class ListItemMonth extends ListItem {

    private DateTime month;

    public ListItemMonth(DateTime month) {
        this.month = month;
    }

    public DateTime getMonth() {
        return month;
    }

    @Override
    public DateTime getDateTime() {
        return month;
    }
}
