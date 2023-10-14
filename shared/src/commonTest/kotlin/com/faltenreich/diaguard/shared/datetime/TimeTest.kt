package com.faltenreich.diaguard.shared.datetime

import com.faltenreich.diaguard.shared.datetime.kotlinx.KotlinxDateTimeFactory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TimeTest {

    private val dateTimeFactory: DateTimeFactory = KotlinxDateTimeFactory()

    @Test
    fun `time is equal if hour and minute and second and millis and nanos are equal`() {
        assertEquals(
            dateTimeFactory.time(hourOfDay = 5, minuteOfHour = 5, secondOfMinute = 5, millisOfSecond = 5, nanosOfMilli = 5),
            dateTimeFactory.time(hourOfDay = 5, minuteOfHour = 5, secondOfMinute = 5, millisOfSecond = 5, nanosOfMilli = 5),
        )
    }

    @Test
    fun `time is sooner if hours are lower`() {
        assertTrue(
            dateTimeFactory.time(hourOfDay = 4, minuteOfHour = 6, secondOfMinute = 6, millisOfSecond = 6, nanosOfMilli = 6)
                <
                dateTimeFactory.time(hourOfDay = 5, minuteOfHour = 5, secondOfMinute = 5, millisOfSecond = 5, nanosOfMilli = 5)
        )
    }

    @Test
    fun `time is sooner if minutes are lower but hour is not`() {
        assertTrue(
            dateTimeFactory.time(hourOfDay = 5, minuteOfHour = 4, secondOfMinute = 6, millisOfSecond = 6, nanosOfMilli = 6)
                <
                dateTimeFactory.time(hourOfDay = 5, minuteOfHour = 5, secondOfMinute = 5, millisOfSecond = 5, nanosOfMilli = 5)
        )
    }

    @Test
    fun `time is sooner if seconds are lower but hours and minutes are not`() {
        assertTrue(
            dateTimeFactory.time(hourOfDay = 5, minuteOfHour = 5, secondOfMinute = 4, millisOfSecond = 6, nanosOfMilli = 6)
                <
                dateTimeFactory.time(hourOfDay = 5, minuteOfHour = 5, secondOfMinute = 5, millisOfSecond = 5, nanosOfMilli = 5)
        )
    }

    @Test
    fun `time is sooner if millis are lower but hours and minutes and seconds are not`() {
        assertTrue(
            dateTimeFactory.time(hourOfDay = 5, minuteOfHour = 5, secondOfMinute = 5, millisOfSecond = 4, nanosOfMilli = 6)
                <
                dateTimeFactory.time(hourOfDay = 5, minuteOfHour = 5, secondOfMinute = 5, millisOfSecond = 5, nanosOfMilli = 5)
        )
    }

    @Test
    fun `time is sooner if nanos are lower but hours and minutes and seconds and nanos are not`() {
        assertTrue(
            dateTimeFactory.time(hourOfDay = 5, minuteOfHour = 5, secondOfMinute = 5, millisOfSecond = 5, nanosOfMilli = 4)
                <
                dateTimeFactory.time(hourOfDay = 5, minuteOfHour = 5, secondOfMinute = 5, millisOfSecond = 5, nanosOfMilli = 5)
        )
    }
}