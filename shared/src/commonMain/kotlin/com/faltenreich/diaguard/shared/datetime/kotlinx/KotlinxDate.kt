package com.faltenreich.diaguard.shared.datetime.kotlinx

import com.faltenreich.diaguard.shared.datetime.Date
import kotlinx.datetime.LocalDate

class KotlinxDate(private val localDate: LocalDate) : Date {

    override val year: Int
        get() = localDate.year

    override val monthOfYear: Int
        get() = localDate.monthNumber

    override val dayOfMonth: Int
        get() = localDate.dayOfMonth

    override fun toString(): String {
        return localDate.toString()
    }
}