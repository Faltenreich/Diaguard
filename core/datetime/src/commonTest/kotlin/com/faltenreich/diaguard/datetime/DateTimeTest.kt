package com.faltenreich.diaguard.datetime

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class DateTimeTest : KoinTest {

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
    fun `minutes until is zero if same date`() {
        val dateTime = dateTimeFactory.dateTime(0)
        assertEquals(0, dateTime.until(dateTime, TimeUnit.MINUTE).inWholeMinutes)
    }
}