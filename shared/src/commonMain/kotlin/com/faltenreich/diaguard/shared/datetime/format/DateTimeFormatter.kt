package com.faltenreich.diaguard.shared.datetime.format

import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.shared.datetime.Time
import com.faltenreich.diaguard.shared.primitive.format

class DateTimeFormatter {

    fun format(date: Date): String {
        return date.run {
            "%02d.%02d.%04d".format(
                dayOfMonth,
                monthOfYear,
                year,
            )
        }
    }

    fun format(time: Time): String {
        return time.run {
            "%02d:%02d".format(
                hourOfDay,
                minuteOfHour,
            )
        }
    }

    fun format(dateTime: DateTime): String {
        return dateTime.run {
            "%s %s".format(
                format(date),
                format(time),
            )
        }
    }
}