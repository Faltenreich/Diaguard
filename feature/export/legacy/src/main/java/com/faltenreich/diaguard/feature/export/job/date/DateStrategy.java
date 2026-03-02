package com.faltenreich.diaguard.feature.export.job.date;

import org.joda.time.DateTime;

public interface DateStrategy {
    DateTime convertDate(DateTime origin);
}
