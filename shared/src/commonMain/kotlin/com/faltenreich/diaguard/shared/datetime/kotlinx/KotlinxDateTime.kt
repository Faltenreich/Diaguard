package com.faltenreich.diaguard.shared.datetime.kotlinx

import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.shared.datetime.Time
import kotlinx.datetime.LocalDateTime

class KotlinxDateTime(
    private val localDateTime: LocalDateTime,
    override val date: Date = KotlinxDate(localDateTime.date),
    override val time: Time = KotlinxTime(localDateTime.time),
) : DateTime, Date by date, Time by time {

    override fun toString(): String {
        return localDateTime.toString()
    }
}