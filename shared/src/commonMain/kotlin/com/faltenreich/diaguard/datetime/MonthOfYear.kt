package com.faltenreich.diaguard.datetime

interface MonthOfYear {

    val year: Int

    val month: Month

    val firstDay: Date

    val lastDay: Date

    fun minus(value: Int, unit: MonthYearUnit): MonthOfYear

    fun plus(value: Int, unit: MonthYearUnit): MonthOfYear

    fun copy(
        year: Int = this.year,
        month: Month = this.month,
    ): MonthOfYear
}