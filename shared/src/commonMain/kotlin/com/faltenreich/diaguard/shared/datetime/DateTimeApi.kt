package com.faltenreich.diaguard.shared.datetime

interface DateTimeApi {

    fun now(): DateTime

    fun convertIsoStringToDateTime(isoString: String): DateTime
}