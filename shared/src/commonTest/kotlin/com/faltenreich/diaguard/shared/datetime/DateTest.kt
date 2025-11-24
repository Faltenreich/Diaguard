package com.faltenreich.diaguard.shared.datetime

import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DateTest : TestSuite() {

    private val dateTimeFactory: DateTimeFactory by inject()

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
}