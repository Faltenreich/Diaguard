package com.faltenreich.diaguard.util;

import com.faltenreich.diaguard.DiaguardApplication;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by Faltenreich on 20.10.2015.
 */
public class DateTimeUtils {

    public static DateTimeFormatter dayMonth() {
        // TODO: Localization
        return DateTimeFormat.forPattern("dd.MM");
    }
}
