package com.faltenreich.diaguard.shared.datetime

import androidx.compose.ui.text.intl.Locale
import androidx.test.platform.app.InstrumentationRegistry
import com.faltenreich.diaguard.datetime.DateTimeAndroidApi
import com.faltenreich.diaguard.datetime.DayOfWeek
import com.faltenreich.diaguard.datetime.WeekOfYear
import com.faltenreich.diaguard.datetime.kotlinx.KotlinxDateTimeFactory
import com.faltenreich.diaguard.localization.ComposeLocalization
import org.junit.Assert
import org.junit.Test

class DateTimeAndroidApiTest {

    @Test
    fun dateFormatsForEnglish() {
        val dateTimePlatformApi = DateTimeAndroidApi(
            localization = ComposeLocalization(locale = Locale("en")),
            context = InstrumentationRegistry.getInstrumentation().context,
        )
        val dateTimeFactory = KotlinxDateTimeFactory(dateTimePlatformApi)
        val date = dateTimeFactory.date(year = 2025, monthNumber = 1, dayOfMonth = 2)
        Assert.assertEquals(
            "Jan 2, 2025",
            dateTimePlatformApi.formatDate(date),
        )
    }

    @Test
    fun dateFormatsForGerman() {
        val dateTimePlatformApi = DateTimeAndroidApi(
            localization = ComposeLocalization(locale = Locale("de")),
            context = InstrumentationRegistry.getInstrumentation().context,
        )
        val dateTimeFactory = KotlinxDateTimeFactory(dateTimePlatformApi)
        val date = dateTimeFactory.date(year = 2025, monthNumber = 1, dayOfMonth = 2)
        Assert.assertEquals(
            "02.01.2025",
            dateTimePlatformApi.formatDate(date),
        )
    }

    @Test
    fun weekStartsOnSundayForEnglish() {
        val dateTimePlatformApi = DateTimeAndroidApi(
            localization = ComposeLocalization(locale = Locale("en")),
            context = InstrumentationRegistry.getInstrumentation().context,
        )
        Assert.assertEquals(
            DayOfWeek.SUNDAY,
            dateTimePlatformApi.getStartOfWeek(),
        )
    }

    @Test
    fun weekStartsOnSundayForGerman() {
        val dateTimePlatformApi = DateTimeAndroidApi(
            localization = ComposeLocalization(locale = Locale("de")),
            context = InstrumentationRegistry.getInstrumentation().context,
        )
        Assert.assertEquals(
            DayOfWeek.MONDAY,
            dateTimePlatformApi.getStartOfWeek(),
        )
    }

    @Test
    fun weekOfYearShouldBe1ForEndOf2017ForEnglish() {
        val dateTimePlatformApi = DateTimeAndroidApi(
            localization = ComposeLocalization(locale = Locale("en")),
            context = InstrumentationRegistry.getInstrumentation().context,
        )
        val dateTimeFactory = KotlinxDateTimeFactory(dateTimePlatformApi)
        val date = dateTimeFactory.date(year = 2017, monthNumber = 12, dayOfMonth = 31)
        Assert.assertEquals(
            WeekOfYear(weekNumber = 1, year = 2018),
            dateTimePlatformApi.weekOfYear(date),
        )
    }

    @Test
    fun weekOfYearShouldBe52ForEndOf2017ForGerman() {
        val dateTimePlatformApi = DateTimeAndroidApi(
            localization = ComposeLocalization(locale = Locale("de")),
            context = InstrumentationRegistry.getInstrumentation().context,
        )
        val dateTimeFactory = KotlinxDateTimeFactory(dateTimePlatformApi)
        val date = dateTimeFactory.date(year = 2017, monthNumber = 12, dayOfMonth = 31)
        Assert.assertEquals(
            WeekOfYear(weekNumber = 52, year = 2017),
            dateTimePlatformApi.weekOfYear(date),
        )
    }
}