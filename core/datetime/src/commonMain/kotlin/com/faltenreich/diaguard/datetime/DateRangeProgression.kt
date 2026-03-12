package com.faltenreich.diaguard.datetime

class DateRangeProgression(
    private val dateRange: DateRange,
    private val step: Int = 1,
    private val intervalDateUnit: DateUnit = DateUnit.DAY,
) : Iterable<DateRange>, ClosedRange<Date> {

    override val start = dateRange.start
    override val endInclusive = dateRange.endInclusive

    override fun iterator(): Iterator<DateRange> {
        return DateIterator(dateRange, step, intervalDateUnit)
    }

    private class DateIterator(
        private val dateRange: DateRange,
        private val step: Int,
        private val intervalDateUnit: DateUnit,
    ) : Iterator<DateRange> {

        private var current = dateRange.start.let { start ->
            start .. start.plus(step, intervalDateUnit).minus(1, DateUnit.DAY)
        }

        override fun hasNext(): Boolean {
            return current.endInclusive <= dateRange.endInclusive
        }

        override fun next(): DateRange {
            if (current.endInclusive > dateRange.endInclusive) {
                throw NoSuchElementException()
            }
            val next = current
            current = current.start.plus(step, intervalDateUnit) ..
                current.endInclusive.plus(step, intervalDateUnit)
            return next
        }
    }
}