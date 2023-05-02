package com.faltenreich.diaguard.shared.datetime

interface DateTime {

    val date: Date

    val time: Time

    val millisSince1970: Long
}