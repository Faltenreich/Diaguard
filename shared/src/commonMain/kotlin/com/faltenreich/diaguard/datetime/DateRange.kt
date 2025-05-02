package com.faltenreich.diaguard.datetime

data class DateRange(
    override val start: Date,
    override val endInclusive: Date,
) : ClosedRange<Date> {

    constructor(date: Date) : this(date, date)
}