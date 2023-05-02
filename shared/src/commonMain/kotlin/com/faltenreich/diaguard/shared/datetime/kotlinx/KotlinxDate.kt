package com.faltenreich.diaguard.shared.datetime.kotlinx

import com.faltenreich.diaguard.shared.datetime.Dateable
import kotlinx.datetime.LocalDate

class KotlinxDate(
    year: Int,
    monthOfYear: Int,
    dayOfMonth: Int,
) : Dateable {

    private val localDate = LocalDate(
        year = year,
        monthNumber = monthOfYear,
        dayOfMonth = dayOfMonth,
    )

    override val year: Int
        get() = localDate.year

    override val monthOfYear: Int
        get() = localDate.monthNumber

    override val dayOfMonth: Int
        get() = localDate.dayOfMonth
}