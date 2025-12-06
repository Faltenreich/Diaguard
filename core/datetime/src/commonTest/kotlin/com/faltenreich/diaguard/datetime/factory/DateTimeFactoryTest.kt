package com.faltenreich.diaguard.datetime.factory

import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.dateTimeModule
import com.faltenreich.diaguard.datetime.testModule
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class DateTimeFactoryTest : KoinTest {

    private val dateTimeFactory: DateTimeFactory by inject()

    @BeforeTest
    fun beforeTest() {
        startKoin { modules(dateTimeModule() + testModule()) }
    }

    @AfterTest
    fun afterTest() {
        stopKoin()
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