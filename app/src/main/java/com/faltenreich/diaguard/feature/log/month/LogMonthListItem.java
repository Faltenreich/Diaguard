package com.faltenreich.diaguard.feature.log.month;

import com.faltenreich.diaguard.feature.log.LogListItem;

import org.joda.time.DateTime;

/**
 * Created by Filip on 10.07.2015.
 */
public class LogMonthListItem extends LogListItem {

    public LogMonthListItem(DateTime dateTime) {
        super(dateTime.withTimeAtStartOfDay());
    }
}
