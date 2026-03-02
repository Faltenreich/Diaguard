package com.faltenreich.diaguard.feature.export.job.date;

import org.joda.time.DateTime;

public class OriginDateStrategy implements DateStrategy {

    @Override
    public DateTime convertDate(DateTime origin) {
        return origin;
    }
}
