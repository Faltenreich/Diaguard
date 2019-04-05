package com.faltenreich.diaguard.ui.list.item;

import org.joda.time.DateTime;

/**
 * Created by Filip on 10.07.2015.
 */
public class ListItemDay extends ListItemDate {

    public ListItemDay(DateTime dateTime) {
        super(dateTime.withTimeAtStartOfDay());
    }
}
