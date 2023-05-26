package com.faltenreich.diaguard.shared.datetime

interface DateTimeFactory<Date : Dateable, Time : Timeable, DateTime : DateTimeable> {

    fun now(): DateTime

    fun fromIsoString(isoString: String): DateTime

    fun fromMillis(millis: Long): DateTime
}