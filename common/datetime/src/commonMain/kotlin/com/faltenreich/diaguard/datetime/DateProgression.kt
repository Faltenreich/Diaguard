package com.faltenreich.diaguard.datetime

class DateProgression(
    override val start: Date,
    override val endInclusive: Date,
    private val stepInDays: Int = 1,
) : Iterable<Date>, ClosedRange<Date> {

    override fun iterator(): Iterator<Date> {
        return DateIterator(start, endInclusive, stepInDays)
    }

    fun toDateRange(): DateRange {
        return start .. endInclusive
    }

    private class DateIterator(
        start: Date,
        private val endInclusive: Date,
        private val stepInDays: Int,
    ) : Iterator<Date> {

        private var current: Date = start

        override fun hasNext(): Boolean {
            return current <= endInclusive
        }

        override fun next(): Date {
            if (current > endInclusive) {
                throw NoSuchElementException()
            }
            val next = current
            current = current.plus(stepInDays, DateUnit.DAY)
            return next
        }
    }
}