package com.faltenreich.diaguard.datetime

import kotlin.test.Test
import kotlin.test.assertEquals

class DayOfWeekTest {

    @Test
    fun `previous day of Monday is Sunday`() {
        assertEquals(
            expected = DayOfWeek.SUNDAY,
            actual = DayOfWeek.MONDAY.previous(),
        )
    }

    @Test
    fun `next day of Sunday is Monday`() {
        assertEquals(
            expected = DayOfWeek.MONDAY,
            actual = DayOfWeek.SUNDAY.next(),
        )
    }
}