package com.faltenreich.diaguard.util.event.ui;

import com.faltenreich.diaguard.util.TimeSpan;
import com.faltenreich.diaguard.util.event.BaseContextEvent;

/**
 * Created by Faltenreich on 23.03.2016.
 */
public class TimeSpanChangedEvent extends BaseContextEvent<TimeSpan> {

    public TimeSpanChangedEvent(TimeSpan timeSpan) {
        super(timeSpan);
    }
}
