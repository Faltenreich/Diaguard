package com.faltenreich.diaguard.shared.datetime

import com.faltenreich.diaguard.shared.serialization.Serializable

interface DateTimeable : Serializable, Comparable<DateTimeable> {

    val date: Date

    val time: Time

    val millisSince1970: Long

    val isoString: String

    override fun compareTo(other: DateTimeable): Int {
        return compareValuesBy(
            this,
            other,
            { it.date },
            { it.time },
        )
    }
}