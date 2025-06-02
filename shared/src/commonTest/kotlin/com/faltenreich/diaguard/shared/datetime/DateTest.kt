package com.faltenreich.diaguard.shared.datetime

import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.WeekOfYear
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.datetime.kotlinx.KotlinxDateTimeFactory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DateTest {

    private val dateTimeFactory: DateTimeFactory = KotlinxDateTimeFactory()

    @Test
    fun `date is equal if year and month and day are equal`() {
        assertEquals(
            dateTimeFactory.date(year = 5, monthNumber = 5, dayOfMonth = 5),
            dateTimeFactory.date(year = 5, monthNumber = 5, dayOfMonth = 5),
        )
    }

    @Test
    fun `date is sooner if years are lower`() {
        assertTrue(
            dateTimeFactory.date(year = 4, monthNumber = 6, dayOfMonth = 6)
                <
                dateTimeFactory.date(year = 5, monthNumber = 5, dayOfMonth = 5)
        )
    }

    @Test
    fun `date is sooner if months are lower but years are not`() {
        assertTrue(
            dateTimeFactory.date(year = 5, monthNumber = 4, dayOfMonth = 6)
                <
                dateTimeFactory.date(year = 5, monthNumber = 5, dayOfMonth = 5)
        )
    }

    @Test
    fun `date is sooner if days are lower but years and months are not`() {
        assertTrue(
            dateTimeFactory.date(year = 5, monthNumber = 5, dayOfMonth = 4)
                <
                dateTimeFactory.date(year = 5, monthNumber = 5, dayOfMonth = 5)
        )
    }

    @Test
    fun `quarter is correct`() {
        assertEquals(1, dateTimeFactory.date(year = 5, monthNumber = 1, dayOfMonth = 1).quarter)
        assertEquals(1, dateTimeFactory.date(year = 5, monthNumber = 2, dayOfMonth = 1).quarter)
        assertEquals(1, dateTimeFactory.date(year = 5, monthNumber = 3, dayOfMonth = 1).quarter)

        assertEquals(2, dateTimeFactory.date(year = 5, monthNumber = 4, dayOfMonth = 1).quarter)
        assertEquals(2, dateTimeFactory.date(year = 5, monthNumber = 5, dayOfMonth = 1).quarter)
        assertEquals(2, dateTimeFactory.date(year = 5, monthNumber = 6, dayOfMonth = 1).quarter)

        assertEquals(3, dateTimeFactory.date(year = 5, monthNumber = 7, dayOfMonth = 1).quarter)
        assertEquals(3, dateTimeFactory.date(year = 5, monthNumber = 8, dayOfMonth = 1).quarter)
        assertEquals(3, dateTimeFactory.date(year = 5, monthNumber = 9, dayOfMonth = 1).quarter)

        assertEquals(4, dateTimeFactory.date(year = 5, monthNumber = 10, dayOfMonth = 1).quarter)
        assertEquals(4, dateTimeFactory.date(year = 5, monthNumber = 11, dayOfMonth = 1).quarter)
        assertEquals(4, dateTimeFactory.date(year = 5, monthNumber = 12, dayOfMonth = 1).quarter)
    }

    @Test
    fun `weekOfYear is 1 if first day of year`() {
        val date = dateTimeFactory.date(year = 5, monthNumber = 1, dayOfMonth = 1)
        assertEquals(WeekOfYear(weekNumber = 1, year = 5), date.weekOfYear)
    }

    @Test
    fun `weekOfYear is 52 if last week of year`() {
        val date = dateTimeFactory.date(year = 5, monthNumber = 1, dayOfMonth = 1)
            .minus(1, DateUnit.WEEK)
        assertEquals(WeekOfYear(weekNumber = 52, year = 4), date.weekOfYear)
    }

    @Test
    fun `weekOfYear is 1 if last day of year`() {
        val date = dateTimeFactory.date(year = 5, monthNumber = 1, dayOfMonth = 1)
            .minus(1, DateUnit.DAY)
        assertEquals(WeekOfYear(weekNumber = 1, year = 5), date.weekOfYear)
    }

    @Test
    fun `start of day is this`() {
        val date = dateTimeFactory.date(year = 2025, monthNumber = 6, dayOfMonth = 5)
        assertEquals(date, date.atStartOf(DateUnit.DAY))
    }

    @Test
    fun `start of week is Monday`() {
        val date = dateTimeFactory.date(year = 2025, monthNumber = 6, dayOfMonth = 5)
        val expected = dateTimeFactory.date(year = 2025, monthNumber = 6, dayOfMonth = 2)
        assertEquals(expected, date.atStartOf(DateUnit.WEEK))
    }

    @Test
    fun `start of month is first day of same month in same year`() {
        val date = dateTimeFactory.date(year = 2025, monthNumber = 6, dayOfMonth = 5)
        val expected = dateTimeFactory.date(year = 2025, monthNumber = 6, dayOfMonth = 1)
        assertEquals(expected, date.atStartOf(DateUnit.MONTH))
    }

    @Test
    fun `start of quarter is first day of three months in same year`() {
        val date = dateTimeFactory.date(year = 2025, monthNumber = 6, dayOfMonth = 5)
        val expected = dateTimeFactory.date(year = 2025, monthNumber = 4, dayOfMonth = 1)
        assertEquals(expected, date.atStartOf(DateUnit.QUARTER))
    }

    @Test
    fun `start of year is first day of same year`() {
        val date = dateTimeFactory.date(year = 2025, monthNumber = 6, dayOfMonth = 5)
        val expected = dateTimeFactory.date(year = 2025, monthNumber = 1, dayOfMonth = 1)
        assertEquals(expected, date.atStartOf(DateUnit.YEAR))
    }

    @Test
    fun `start of century is first day of same century`() {
        val date = dateTimeFactory.date(year = 2025, monthNumber = 6, dayOfMonth = 5)
        val expected = dateTimeFactory.date(year = 2000, monthNumber = 1, dayOfMonth = 1)
        assertEquals(expected, date.atStartOf(DateUnit.CENTURY))
    }
}