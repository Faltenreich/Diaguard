package com.faltenreich.diaguard.datetime.kotlinx

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.Month
import com.faltenreich.diaguard.datetime.MonthOfYear
import com.faltenreich.diaguard.datetime.MonthYearUnit

class KotlinxMonthOfYear(
    year: Int,
    monthNumber: Int,
) : MonthOfYear {

    private var yearDelegate = year
    private var monthDelegate = kotlinx.datetime.Month(number = monthNumber)

    override val month: Month
        get() = Month.fromMonthNumber(monthDelegate.ordinal + 1)

    override val year: Int
        get() = yearDelegate

    override val firstDay: Date
        get() = KotlinxDate(year = year, monthNumber = month.monthNumber, dayOfMonth = 1)

    override val lastDay: Date
        get() = firstDay
            .plus(1, DateUnit.MONTH)
            .minus(1, DateUnit.DAY)

    override fun minus(value: Int, unit: MonthYearUnit): MonthOfYear {
        return when (unit) {
            MonthYearUnit.MONTH -> KotlinxDate(
                year = year,
                monthNumber = month.monthNumber,
                dayOfMonth = 1,
            ).minus(value, DateUnit.MONTH).monthOfYear
            MonthYearUnit.YEAR -> copy(year = year - value)
        }
    }

    override fun plus(value: Int, unit: MonthYearUnit): MonthOfYear {
        return when (unit) {
            MonthYearUnit.MONTH -> KotlinxDate(
                year = year,
                monthNumber = month.monthNumber,
                dayOfMonth = 1,
            ).plus(value, DateUnit.MONTH).monthOfYear
            MonthYearUnit.YEAR -> copy(year = year + value)
        }
    }

    override fun copy(year: Int, month: Month): MonthOfYear {
        return KotlinxMonthOfYear(
            year = year,
            monthNumber = month.monthNumber,
        )
    }
}