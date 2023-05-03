package com.faltenreich.diaguard.shared.datetime

import com.faltenreich.diaguard.shared.datetime.kotlinx.KotlinxDate
import com.faltenreich.diaguard.shared.datetime.kotlinx.KotlinxDateTime
import com.faltenreich.diaguard.shared.datetime.kotlinx.KotlinxTime

class DateTime(
    year: Int,
    monthOfYear: Int,
    dayOfMonth: Int,
    hourOfDay: Int,
    minuteOfHour: Int,
    secondOfMinute: Int,
    millisOfSecond: Int,
    nanosOfMillis: Int,
) : Dateable by KotlinxDate(
    year = year,
    monthOfYear = monthOfYear,
    dayOfMonth = dayOfMonth,
), Timeable by KotlinxTime(
    hourOfDay = hourOfDay,
    minuteOfHour = minuteOfHour,
    secondOfMinute = secondOfMinute,
    millisOfSecond = millisOfSecond,
    nanosOfMillis = nanosOfMillis,
), DateTimeable by KotlinxDateTime(
    year = year,
    monthOfYear = monthOfYear,
    dayOfMonth = dayOfMonth,
    hourOfDay = hourOfDay,
    minuteOfHour = minuteOfHour,
    secondOfMinute = secondOfMinute,
    millisOfSecond = millisOfSecond,
    nanosOfMillis = nanosOfMillis,
) {

    private constructor(kotlinxDateTime: KotlinxDateTime) : this(
        year = kotlinxDateTime.date.year,
        monthOfYear = kotlinxDateTime.date.monthOfYear,
        dayOfMonth = kotlinxDateTime.date.dayOfMonth,
        hourOfDay = kotlinxDateTime.time.hourOfDay,
        minuteOfHour = kotlinxDateTime.time.minuteOfHour,
        secondOfMinute = kotlinxDateTime.time.secondOfMinute,
        millisOfSecond = kotlinxDateTime.time.millisOfSecond,
        nanosOfMillis = kotlinxDateTime.time.nanosOfMillis,
    )

    constructor(isoString: String) : this(KotlinxDateTime(isoString = isoString))

    constructor(millis: Long) : this(KotlinxDateTime(millis = millis))

    companion object {

        fun now(): DateTime {
            return DateTime(KotlinxDateTime.now())
        }
    }
}