package com.faltenreich.diaguard.shared.datetime

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class KotlinxDateTimeApi(
    private val mapper: KotlinxDateTimeMapper,
) : DateTimeApi {

    override fun now(): DateTime {
        val localDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        return mapper.map(localDateTime)
    }

    override fun createDateTimeFromIsoString(isoString: String): DateTime {
        val localDateTime = LocalDateTime.parse(isoString = isoString)
        return mapper.map(localDateTime)
    }
}