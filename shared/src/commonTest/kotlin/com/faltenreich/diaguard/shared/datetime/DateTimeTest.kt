package com.faltenreich.diaguard.shared.datetime

import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.datetime.TimeUnit
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertEquals

class DateTimeTest : TestSuite {

    private val dateTimeFactory: DateTimeFactory by inject()

    @Test
    fun `minutes until is zero if same date`() {
        val dateTime = dateTimeFactory.dateTime(0)
        assertEquals(0, dateTime.until(dateTime, TimeUnit.MINUTE).inWholeMinutes)
    }
}