package com.faltenreich.diaguard.shared.datetime

import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.primitive.format
import com.faltenreich.diaguard.shared.serialization.ObjectInputStream
import com.faltenreich.diaguard.shared.serialization.ObjectOutputStream
import com.faltenreich.diaguard.shared.serialization.Serializable
import org.koin.core.parameter.parametersOf

class DateTime(
    year: Int,
    monthNumber: Int,
    dayOfMonth: Int,
    hourOfDay: Int,
    minuteOfHour: Int,
    secondOfMinute: Int,
    millisOfSecond: Int,
    nanosOfMilli: Int,
) : Serializable, Comparable<DateTime> {

    private var delegate: DateTimeable = inject {
        parametersOf(
            year,
            monthNumber,
            dayOfMonth,
            hourOfDay,
            minuteOfHour,
            secondOfMinute,
            millisOfSecond,
            nanosOfMilli,
        )
    }

    val date: Date
        get() = delegate.date

    val time: Time
        get() = delegate.time

    val millisSince1970: Long
        get() = delegate.millisSince1970

    val isoString: String
        get() = delegate.isoString

    constructor(dateTimeable: DateTimeable) : this(
        year = dateTimeable.date.year,
        monthNumber = dateTimeable.date.monthNumber,
        dayOfMonth = dateTimeable.date.dayOfMonth,
        hourOfDay = dateTimeable.time.hourOfDay,
        minuteOfHour = dateTimeable.time.minuteOfHour,
        secondOfMinute = dateTimeable.time.secondOfMinute,
        millisOfSecond = dateTimeable.time.millisOfSecond,
        nanosOfMilli = dateTimeable.time.nanosOfMilli,
    )

    constructor(
        isoString: String,
        factory: DateTimeFactory<Dateable, Timeable, DateTimeable> = inject(),
    ) : this(
        dateTimeable = factory.fromIsoString(isoString),
    )

    constructor(
        millis: Long,
        factory: DateTimeFactory<Dateable, Timeable, DateTimeable> = inject(),
    ) : this(
        dateTimeable = factory.fromMillis(millis),
    )

    fun now(): DateTime {
        return DateTime(delegate.now())
    }

    override fun compareTo(other: DateTime): Int {
        return when {
            date > other.date -> 1
            date < other.date -> -1
            time > other.time -> 1
            time < other.time -> -1
            else -> 0
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is DateTime &&
            date == other.date &&
            time == other.time
    }

    override fun hashCode(): Int {
        return date.hashCode() + time.hashCode()
    }

    override fun toString(): String {
        return "%s %s".format(
            date.toString(),
            time.toString(),
        )
    }

    @Suppress("unused")
    private fun readObject(inputStream: ObjectInputStream) {
        delegate.readObject(inputStream)
    }

    @Suppress("unused")
    private fun writeObject(outputStream: ObjectOutputStream) {
        delegate.writeObject(outputStream)
    }

    companion object {

        private val factory = inject<DateTimeFactory<Dateable, Timeable, DateTimeable>>()

        fun now(): DateTime {
            return DateTime(factory.now())
        }
    }
}