package com.faltenreich.diaguard.shared.datetime.kotlinx

import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.shared.datetime.Time
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

class KotlinxDateTime(
    private val localDateTime: LocalDateTime,
) : DateTime {


    override val date: Date
        get() = KotlinxDate(localDateTime.date)

    override val time: Time
    get() = KotlinxTime(localDateTime.time)

    override val millisSince1970: Long
        get() = localDateTime.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()

    override fun toString(): String {
        return localDateTime.toString()
    }
}