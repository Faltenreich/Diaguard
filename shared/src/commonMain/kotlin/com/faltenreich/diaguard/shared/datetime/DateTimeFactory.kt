package com.faltenreich.diaguard.shared.datetime

interface DateTimeFactory {

    fun now(): DateTimeable

    fun at(year: Int, monthNumber: Int, dayOfMonth: Int): Date

    fun today(): Date

    fun fromIsoString(isoString: String): DateTimeable

    fun fromMillis(millis: Long): DateTimeable
}