package com.faltenreich.diaguard.datetime

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.test.TestSuite
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertEquals

class DateTimeTest : TestSuite(modules = dateTimeModule() + testModule()) {

    private val dateTimeFactory: DateTimeFactory by inject()

    @Test
    fun `minutes until is zero if same date`() {
        val dateTime = dateTimeFactory.dateTime(0)
        assertEquals(0, dateTime.until(dateTime, TimeUnit.MINUTE).inWholeMinutes)
    }
}