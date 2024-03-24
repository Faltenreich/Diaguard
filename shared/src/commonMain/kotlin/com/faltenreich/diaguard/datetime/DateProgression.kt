package com.faltenreich.diaguard.datetime

class DateProgression(
    override val start: Date,
    override val endInclusive: Date,
    private val stepInDays: Int = 1,
) : Iterable<Date>, ClosedRange<Date> {

    override fun iterator(): Iterator<Date> {
        return DateIterator(start, endInclusive, stepInDays)
    }
}