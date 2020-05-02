package com.faltenreich.diaguard.feature.log.pending;

import com.faltenreich.diaguard.feature.log.LogListItem;

import org.joda.time.DateTime;

/**
 * Created by Filip on 10.07.2015.
 */
public class LogPendingListItem extends LogListItem {

    public LogPendingListItem(DateTime dateTime) {
        super(dateTime);
    }
}
