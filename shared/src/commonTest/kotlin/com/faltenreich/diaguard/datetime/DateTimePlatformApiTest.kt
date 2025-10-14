package com.faltenreich.diaguard.datetime

import androidx.compose.ui.text.intl.Locale
import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.shared.localization.FakeLocalization
import com.faltenreich.diaguard.shared.localization.Localization
import org.koin.test.inject
import org.koin.test.mock.declare
import kotlin.test.Test
import kotlin.test.assertEquals

class DateTimePlatformApiTest : TestSuite {

    private val dateTimeFactory: DateTimeFactory by inject()
    private val platformApi: DateTimePlatformApi by inject()

    @Test
    fun `weekOfYear is 52 on 2017-12-31 if week starts at Sunday`() {
        declare<Localization> { FakeLocalization(locale = Locale("en")) }

        val date = dateTimeFactory.date(year = 2017, monthNumber = 12, dayOfMonth = 31)
        assertEquals(
            expected = WeekOfYear(weekNumber = 52, year = 2017),
            actual = platformApi.weekOfYear(date),
        )
    }

    @Test
    fun `weekOfYear is 1 on 2017-12-31 if week starts at Monday`() {
        declare<Localization> { FakeLocalization(locale = Locale("de")) }

        val date = dateTimeFactory.date(year = 2017, monthNumber = 12, dayOfMonth = 31)
        assertEquals(
            expected = WeekOfYear(weekNumber = 1, year = 2018),
            actual = platformApi.weekOfYear(date),
        )
    }
}