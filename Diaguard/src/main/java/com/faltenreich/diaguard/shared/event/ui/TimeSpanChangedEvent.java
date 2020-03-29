package com.faltenreich.diaguard.shared.event.ui;

import com.faltenreich.diaguard.shared.event.BaseContextEvent;
import com.faltenreich.diaguard.feature.datetime.TimeSpan;

/**
 * Created by Faltenreich on 23.03.2016.
 */
public class TimeSpanChangedEvent extends BaseContextEvent<TimeSpan> {

    public TimeSpanChangedEvent(TimeSpan timeSpan) {
        super(timeSpan);
    }
}
