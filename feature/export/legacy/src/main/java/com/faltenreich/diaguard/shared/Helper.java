package com.faltenreich.diaguard.shared;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Helper {

    public static DateTimeFormatter getDateFormat() {
        return DateTimeFormat.mediumDate();
    }
}
