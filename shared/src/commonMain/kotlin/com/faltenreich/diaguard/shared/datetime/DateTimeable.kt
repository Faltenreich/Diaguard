package com.faltenreich.diaguard.shared.datetime

interface DateTimeable {

    val date: Date

    val time: Time

    val millisSince1970: Long

    val isoString: String
}