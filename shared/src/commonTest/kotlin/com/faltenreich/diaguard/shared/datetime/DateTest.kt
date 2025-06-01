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
}