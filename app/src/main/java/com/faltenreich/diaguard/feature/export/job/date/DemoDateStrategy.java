package com.faltenreich.diaguard.feature.export.job.date;

import org.joda.time.DateTime;
import org.joda.time.Days;

public class DemoDateStrategy implements DateStrategy {

    private DateTime maxDate;

    public DemoDateStrategy(DateTime maxDate) {
        this.maxDate = maxDate;
    }

    @Override
    public DateTime convertDate(DateTime origin) {
        int differenceInDays = Days.daysBetween(origin, maxDate).getDays();
        return DateTime.now().minusDays(differenceInDays);
    }
}
