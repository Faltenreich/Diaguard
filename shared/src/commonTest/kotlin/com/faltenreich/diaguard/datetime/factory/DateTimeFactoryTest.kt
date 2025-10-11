package com.faltenreich.diaguard.datetime.factory

import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.WeekOfYear
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertEquals

class DateTimeFactoryTest : TestSuite {

    private val dateTimeFactory: DateTimeFactory by inject()

    @Test
    fun `weekOfYear is 1 if first day of year`() {
        val date = dateTimeFactory.date(year = 5, monthNumber = 1, dayOfMonth = 1)
        assertEquals(
            expected = WeekOfYear(weekNumber = 1, year = 5),
            actual = dateTimeFactory.weekOfYear(date),
        )
    }

    @Test
    fun `weekOfYear is 52 if last week of year`() {
        val date = dateTimeFactory.date(year = 5, monthNumber = 1, dayOfMonth = 1)
            .minus(1, DateUnit.WEEK)
        assertEquals(
            expected = WeekOfYear(weekNumber = 52, year = 4),
            actual = dateTimeFactory.weekOfYear(date),
        )
    }

    @Test
    fun `weekOfYear is 1 if last day of year`() {
        val date = dateTimeFactory.date(year = 5, monthNumber = 1, dayOfMonth = 1)
            .minus(1, DateUnit.DAY)
        assertEquals(
            expected = WeekOfYear(weekNumber = 1, year = 5),
            actual = dateTimeFactory.weekOfYear(date),
        )
    }

    @Test
    fun `start of day is this`() {
        val date = dateTimeFactory.date(year = 2025, monthNumber = 6, dayOfMonth = 5)
        assertEquals(
            expected = date,
            actual = dateTimeFactory.dateAtStartOf(date, DateUnit.DAY),
        )
    }

    @Test
    fun `start of week is Monday`() {
        assertEquals(
            expected = dateTimeFactory.date(year = 2025, monthNumber = 6, dayOfMonth = 2),
            actual = dateTimeFactory.dateAtStartOf(
                date = dateTimeFactory.date(year = 2025, monthNumber = 6, dayOfMonth = 5),
                unit = DateUnit.WEEK,
            ),
        )
    }

    @Test
    fun `start of month is first day of same month in same year`() {
        assertEquals(
            expected = dateTimeFactory.date(year = 2025, monthNumber = 6, dayOfMonth = 1),
            actual = dateTimeFactory.dateAtStartOf(
                date = dateTimeFactory.date(year = 2025, monthNumber = 6, dayOfMonth = 5),
                unit = DateUnit.MONTH,
            ),
        )
    }

    @Test
    fun `start of quarter is first day of three months in same year`() {
        assertEquals(
            expected = dateTimeFactory.date(year = 2025, monthNumber = 4, dayOfMonth = 1),
            actual = dateTimeFactory.dateAtStartOf(
                date = dateTimeFactory.date(year = 2025, monthNumber = 6, dayOfMonth = 5),
                unit = DateUnit.QUARTER,
            ),
        )
    }

    @Test
    fun `start of year is first day of same year`() {
        assertEquals(
            expected = dateTimeFactory.date(year = 2025, monthNumber = 1, dayOfMonth = 1),
            actual = dateTimeFactory.dateAtStartOf(
                date = dateTimeFactory.date(year = 2025, monthNumber = 6, dayOfMonth = 5),
                unit = DateUnit.YEAR,
            ),
        )
    }

    @Test
    fun `start of century is first day of same century`() {
        assertEquals(
            expected = dateTimeFactory.date(year = 2000, monthNumber = 1, dayOfMonth = 1),
            actual = dateTimeFactory.dateAtStartOf(
                date = dateTimeFactory.date(year = 2025, monthNumber = 6, dayOfMonth = 5),
                unit = DateUnit.CENTURY,
            ),
        )
    }
}