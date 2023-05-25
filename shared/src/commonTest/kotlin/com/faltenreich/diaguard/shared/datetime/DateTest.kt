package com.faltenreich.diaguard.shared.datetime

import kotlin.test.Test
import kotlin.test.assertTrue

class DateTest {

    @Test
    fun `date is comparable`() {
        // FIXME: Make date and time comparable
        val sooner = Date(year = 0, monthOfYear = 0, dayOfMonth = 0)
        val later = Date(year = 0, monthOfYear = 0, dayOfMonth = 1)
        assertTrue(sooner < later)
    }
}