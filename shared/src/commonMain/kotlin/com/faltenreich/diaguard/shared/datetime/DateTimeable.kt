package com.faltenreich.diaguard.shared.datetime

import com.faltenreich.diaguard.shared.serialization.Serializable

interface DateTimeable : Serializable {

    val date: Date

    val time: Time

    val millisSince1970: Long

    val isoString: String
}