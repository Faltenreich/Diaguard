package com.faltenreich.diaguard.shared.datetime

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.datetime.kotlinx.KotlinxDateTimeFactory
import kotlin.test.Test
import kotlin.test.assertEquals

class DateTimeTest {

    private val dateTimeFactory: DateTimeFactory = KotlinxDateTimeFactory()

    @Test
    fun `minutes until is zero if same date`() {
        val dateTime = dateTimeFactory.dateTime(0)
        assertEquals(0, dateTime.minutesUntil(dateTime))
    }
}