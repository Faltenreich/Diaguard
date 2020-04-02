package com.faltenreich.diaguard.feature.log.day;

import com.faltenreich.diaguard.feature.log.LogListItem;

import org.joda.time.DateTime;

/**
 * Created by Filip on 10.07.2015.
 */
public class LogDayListItem extends LogListItem {

    public LogDayListItem(DateTime dateTime) {
        super(dateTime.withTimeAtStartOfDay());
    }
}
