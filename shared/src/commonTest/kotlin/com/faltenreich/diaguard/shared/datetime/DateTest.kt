package com.faltenreich.diaguard.shared.datetime

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DateTest {

    @Test
    fun `date is equal if year and month and day are equal`() {
        assertEquals(
            Date(year = 5, monthNumber = 5, dayOfMonth = 5),
            Date(year = 5, monthNumber = 5, dayOfMonth = 5),
        )
    }

    @Test
    fun `date is sooner if years are lower`() {
        assertTrue(
            Date(year = 4, monthNumber = 6, dayOfMonth = 6)
                <
                Date(year = 5, monthNumber = 5, dayOfMonth = 5)
        )
    }

    @Test
    fun `date is sooner if months are lower but years are not`() {
        assertTrue(
            Date(year = 5, monthNumber = 4, dayOfMonth = 6)
                <
                Date(year = 5, monthNumber = 5, dayOfMonth = 5)
        )
    }

    @Test
    fun `date is sooner if days are lower but years and months are not`() {
        assertTrue(
            Date(year = 5, monthNumber = 5, dayOfMonth = 4)
                <
                Date(year = 5, monthNumber = 5, dayOfMonth = 5)
        )
    }
}