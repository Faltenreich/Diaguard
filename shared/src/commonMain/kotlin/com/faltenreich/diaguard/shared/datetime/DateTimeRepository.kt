package com.faltenreich.diaguard.shared.datetime

import org.koin.core.annotation.Single

@Single
class DateTimeRepository(
    private val api: DateTimeApi,
) {

    fun now(): DateTime {
        return api.now()
    }

    fun convertIsoStringToDateTime(isoString: String): DateTime {
        return api.convertIsoStringToDateTime(isoString)
    }
}