package com.faltenreich.diaguard.shared.datetime

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TimeTest {

    @Test
    fun `time is equal if hour and minute and second and millis and nanos are equal`() {
        assertEquals(
            Time(hourOfDay = 5, minuteOfHour = 5, secondOfMinute = 5, millisOfSecond = 5, nanosOfMillis = 5),
            Time(hourOfDay = 5, minuteOfHour = 5, secondOfMinute = 5, millisOfSecond = 5, nanosOfMillis = 5),
        )
    }

    @Test
    fun `time is sooner if hours are lower`() {
        assertTrue(
            Time(hourOfDay = 4, minuteOfHour = 6, secondOfMinute = 6, millisOfSecond = 6, nanosOfMillis = 6)
                <
                Time(hourOfDay = 5, minuteOfHour = 5, secondOfMinute = 5, millisOfSecond = 5, nanosOfMillis = 5)
        )
    }

    @Test
    fun `time is sooner if minutes are lower but hour is not`() {
        assertTrue(
            Time(hourOfDay = 5, minuteOfHour = 4, secondOfMinute = 6, millisOfSecond = 6, nanosOfMillis = 6)
                <
                Time(hourOfDay = 5, minuteOfHour = 5, secondOfMinute = 5, millisOfSecond = 5, nanosOfMillis = 5)
        )
    }

    @Test
    fun `time is sooner if seconds are lower but hours and minutes are not`() {
        assertTrue(
            Time(hourOfDay = 5, minuteOfHour = 5, secondOfMinute = 4, millisOfSecond = 6, nanosOfMillis = 6)
                <
                Time(hourOfDay = 5, minuteOfHour = 5, secondOfMinute = 5, millisOfSecond = 5, nanosOfMillis = 5)
        )
    }

    @Test
    fun `time is sooner if millis are lower but hours and minutes and seconds are not`() {
        assertTrue(
            Time(hourOfDay = 5, minuteOfHour = 5, secondOfMinute = 5, millisOfSecond = 4, nanosOfMillis = 6)
                <
                Time(hourOfDay = 5, minuteOfHour = 5, secondOfMinute = 5, millisOfSecond = 5, nanosOfMillis = 5)
        )
    }

    @Test
    fun `time is sooner if nanos are lower but hours and minutes and seconds and nanos are not`() {
        assertTrue(
            Time(hourOfDay = 5, minuteOfHour = 5, secondOfMinute = 5, millisOfSecond = 5, nanosOfMillis = 4)
                <
                Time(hourOfDay = 5, minuteOfHour = 5, secondOfMinute = 5, millisOfSecond = 5, nanosOfMillis = 5)
        )
    }
}