package com.faltenreich.diaguard.shared.datetime

class DateIterator(
    start: Date,
    private val endInclusive: Date,
    private val stepInDays: Int,
) : Iterator<Date> {

    private var current: Date = start

    override fun hasNext(): Boolean {
        return current <= endInclusive
    }

    override fun next(): Date {
        val next = current
        current = current.plusDays(stepInDays)
        return next
    }
}