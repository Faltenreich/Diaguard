package com.faltenreich.diaguard.shared.datetime

interface DateTimeFactory {

    fun now(): DateTimeable

    fun fromIsoString(isoString: String): DateTimeable

    fun fromMillis(millis: Long): DateTimeable
}