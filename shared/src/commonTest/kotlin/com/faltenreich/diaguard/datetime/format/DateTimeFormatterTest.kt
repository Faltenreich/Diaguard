package com.faltenreich.diaguard.datetime.format

import androidx.compose.ui.text.intl.Locale
import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.shared.localization.FakeLocalization
import com.faltenreich.diaguard.shared.localization.Localization
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.date_time_ago_days
import diaguard.shared.generated.resources.date_time_ago_hours
import diaguard.shared.generated.resources.date_time_ago_minutes
import diaguard.shared.generated.resources.date_time_ago_moments
import diaguard.shared.generated.resources.month_january
import diaguard.shared.generated.resources.month_january_short
import diaguard.shared.generated.resources.weekday_saturday
import diaguard.shared.generated.resources.weekday_saturday_short
import org.koin.test.inject
import org.koin.test.mock.declare
import kotlin.test.Test
import kotlin.test.assertEquals

class DateTimeFormatterTest : TestSuite {

    private val dateTimeFactory: DateTimeFactory by inject()
    private val dateTimeFormatter: DateTimeFormatter by inject()

    @Test
    fun `format time for 24-hour format`() {
        declare<Localization> { FakeLocalization(is24HourFormat = true) }

        assertEquals(
            expected = "13:12",
            actual = dateTimeFormatter.formatTime(
                time = dateTimeFactory.time(hourOfDay = 13, minuteOfHour = 12),
            ),
        )
    }

    @Test
    fun `format time for 12-hour format`() {
        declare<Localization> { FakeLocalization(is24HourFormat = false) }

        assertEquals(
            expected = "01:12 PM",
            actual = dateTimeFormatter.formatTime(
                time = dateTimeFactory.time(hourOfDay = 13, minuteOfHour = 12),
            ),
        )
    }

    @Test
    fun `format time passed if moments ago`() {
        val start = dateTimeFactory.dateTime(
            year = 2000,
            monthNumber = 1,
            dayOfMonth = 1,
            hourOfDay = 0,
            minuteOfHour = 0,
        )
        val end = start
        assertEquals(
            expected = Res.string.date_time_ago_moments.key,
            actual = dateTimeFormatter.formatTimePassed(start, end),
        )
    }

    @Test
    fun `format time passed if minutes ago`() {
        val start = dateTimeFactory.dateTime(
            year = 2000,
            monthNumber = 1,
            dayOfMonth = 1,
            hourOfDay = 0,
            minuteOfHour = 0,
        )
        val end = start.copy(
            minuteOfHour = 2,
        )
        assertEquals(
            expected = Res.string.date_time_ago_minutes.key,
            actual = dateTimeFormatter.formatTimePassed(start, end),
        )
    }

    @Test
    fun `format time passed if hours ago`() {
        val start = dateTimeFactory.dateTime(
            year = 2000,
            monthNumber = 1,
            dayOfMonth = 1,
            hourOfDay = 0,
            minuteOfHour = 0,
        )
        val end = start.copy(
            dayOfMonth = 2,
        )
        assertEquals(
            expected = Res.string.date_time_ago_hours.key,
            actual = dateTimeFormatter.formatTimePassed(start, end),
        )
    }

    @Test
    fun `format time passed if days ago`() {
        val start = dateTimeFactory.dateTime(
            year = 2000,
            monthNumber = 1,
            dayOfMonth = 1,
            hourOfDay = 0,
            minuteOfHour = 0,
        )
        val end = start.copy(
            dayOfMonth = 3,
        )
        assertEquals(
            expected = Res.string.date_time_ago_days.key,
            actual = dateTimeFormatter.formatTimePassed(start, end),
        )
    }

    @Test
    fun `format date for German`() {
        declare<Localization> { FakeLocalization(locale = Locale("de")) }

        assertEquals(
            expected = "02.01.2000",
            actual = dateTimeFormatter.formatDate(
                date = dateTimeFactory.date(
                    year = 2000,
                    monthNumber = 1,
                    dayOfMonth = 2,
                ),
            ),
        )
    }

    @Test
    fun `format date for English`() {
        declare<Localization> { FakeLocalization(locale = Locale("en")) }

        assertEquals(
            expected = "Jan 2, 2000",
            actual = dateTimeFormatter.formatDate(
                date = dateTimeFactory.date(
                    year = 2000,
                    monthNumber = 1,
                    dayOfMonth = 2,
                ),
            ),
        )
    }

    @Test
    fun `format date time`() {
        declare<Localization> { FakeLocalization(locale = Locale("en")) }

        assertEquals(
            expected = "Jan 2, 2000 03:04",
            actual = dateTimeFormatter.formatDateTime(
                dateTime = dateTimeFactory.dateTime(
                    year = 2000,
                    monthNumber = 1,
                    dayOfMonth = 2,
                    hourOfDay = 3,
                    minuteOfHour = 4,
                ),
            ),
        )
    }

    @Test
    fun `format week`() {
        assertEquals(
            expected = "1",
            actual = dateTimeFormatter.formatWeek(
                date = dateTimeFactory.date(
                    year = 2000,
                    monthNumber = 1,
                    dayOfMonth = 1,
                )
            ),
        )
        assertEquals(
            expected = "2",
            actual = dateTimeFormatter.formatWeek(
                date = dateTimeFactory.date(
                    year = 2000,
                    monthNumber = 1,
                    dayOfMonth = 2,
                )
            ),
        )
    }

    @Test
    fun `format day of week`() {
        assertEquals(
            expected = Res.string.weekday_saturday.key,
            actual = dateTimeFormatter.formatDayOfWeek(
                date = dateTimeFactory.date(
                    year = 2000,
                    monthNumber = 1,
                    dayOfMonth = 1,
                ),
                abbreviated = false,
            ),
        )
        assertEquals(
            expected = Res.string.weekday_saturday_short.key,
            actual = dateTimeFormatter.formatDayOfWeek(
                date = dateTimeFactory.date(
                    year = 2000,
                    monthNumber = 1,
                    dayOfMonth = 1,
                ),
                abbreviated = true,
            ),
        )
    }

    @Test
    fun `format day of month`() {
        assertEquals(
            expected = "01",
            actual = dateTimeFormatter.formatDayOfMonth(
                date = dateTimeFactory.date(
                    year = 2000,
                    monthNumber = 1,
                    dayOfMonth = 1,
                ),
            ),
        )
    }

    @Test
    fun `format month`() {
        assertEquals(
            expected = Res.string.month_january.key,
            actual = dateTimeFormatter.formatMonth(
                month = dateTimeFactory.date(
                    year = 2000,
                    monthNumber = 1,
                    dayOfMonth = 1,
                ).month,
                abbreviated = false,
            ),
        )
        assertEquals(
            expected = Res.string.month_january_short.key,
            actual = dateTimeFormatter.formatMonth(
                month = dateTimeFactory.date(
                    year = 2000,
                    monthNumber = 1,
                    dayOfMonth = 1,
                ).month,
                abbreviated = true,
            ),
        )
    }

    @Test
    fun `format quarter`() {
        assertEquals(
            expected = "1",
            actual = dateTimeFormatter.formatQuarter(
                date = dateTimeFactory.date(
                    year = 2000,
                    monthNumber = 1,
                    dayOfMonth = 1,
                ),
            ),
        )
        assertEquals(
            expected = "2",
            actual = dateTimeFormatter.formatQuarter(
                date = dateTimeFactory.date(
                    year = 2000,
                    monthNumber = 4,
                    dayOfMonth = 1,
                ),
            ),
        )
    }

    @Test
    fun `format year`() {
        assertEquals(
            expected = "2000",
            actual = dateTimeFormatter.formatYear(
                date = dateTimeFactory.date(
                    year = 2000,
                    monthNumber = 1,
                    dayOfMonth = 1,
                ),
            ),
        )
    }
}