package com.faltenreich.diaguard.shared.datetime.kotlinx

import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.shared.datetime.Time
import kotlinx.datetime.LocalDateTime

class KotlinxDateTime(
    private val localDateTime: LocalDateTime,
) : DateTime {


    override val date: Date
        get() = KotlinxDate(localDateTime.date)

    override val time: Time
    get() = KotlinxTime(localDateTime.time)

    override fun toString(): String {
        return localDateTime.toString()
    }
}