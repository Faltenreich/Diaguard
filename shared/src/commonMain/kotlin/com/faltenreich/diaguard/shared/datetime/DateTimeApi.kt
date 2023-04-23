package com.faltenreich.diaguard.shared.datetime

interface DateTimeApi {

    fun now(): DateTime

    fun createDateTimeFromIsoString(isoString: String): DateTime
}