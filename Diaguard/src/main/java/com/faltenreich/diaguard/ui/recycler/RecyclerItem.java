package com.faltenreich.diaguard.ui.recycler;

import org.joda.time.DateTime;

/**
 * Created by Filip on 10.07.2015.
 */
public class RecyclerItem {

    private DateTime dateTime;

    public RecyclerItem(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public DateTime getDateTime() {
        return dateTime;
    }
}
