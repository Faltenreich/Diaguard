@file:Suppress("MagicNumber")

package com.faltenreich.diaguard.datetime.kotlinx

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.DayOfWeek
import com.faltenreich.diaguard.datetime.DayOfWeek.FRIDAY
import com.faltenreich.diaguard.datetime.DayOfWeek.MONDAY
import com.faltenreich.diaguard.datetime.DayOfWeek.SATURDAY
import com.faltenreich.diaguard.datetime.DayOfWeek.SUNDAY
import com.faltenreich.diaguard.datetime.DayOfWeek.THURSDAY
import com.faltenreich.diaguard.datetime.DayOfWeek.TUESDAY
import com.faltenreich.diaguard.datetime.DayOfWeek.WEDNESDAY
import com.faltenreich.diaguard.datetime.Time
import com.faltenreich.diaguard.datetime.WeekOfYear
import com.faltenreich.diaguard.shared.serialization.ObjectInputStream
import com.faltenreich.diaguard.shared.serialization.ObjectOutputStream
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.daysUntil
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.plus
import kotlin.math.floor

class KotlinxDate(private var delegate: LocalDate) : Date {

    override val year: Int get() = delegate.year
    override val monthNumber: Int get() = delegate.month.number
    // TODO: Replace with official solution when ready
    //  https://github.com/Kotlin/kotlinx-datetime/issues/129
    override val weekOfYear: WeekOfYear get() {
        var weekNumber = 1
        var comparison = LocalDate(year = year, month = 1, day = 1)
        // Start at end of week
        val endOfWeek = week.last()
        while (comparison.dayOfWeek != endOfWeek.fromDomain()) {
            comparison = comparison.plus(1 , DateTimeUnit.DAY)
        }
        // Stop when passing date
        while (comparison <= delegate) {
            comparison = comparison.plus(1, DateTimeUnit.WEEK)
            weekNumber += 1
        }
        // Handle first week of next year that starts in this year
        if (year != comparison.year) {
            weekNumber = 1
        }
        return WeekOfYear(
            weekNumber = weekNumber,
            year = comparison.year,
        )
    }
    override val dayOfMonth: Int get() = delegate.day
    override val dayOfWeek: DayOfWeek get() = delegate.dayOfWeek.toDomain()
    // TODO: Localize
    override val week: List<DayOfWeek> = listOf(
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY,
        SUNDAY,
    )

    constructor(
        year: Int,
        monthNumber: Int,
        dayOfMonth: Int,
    ) : this(
        LocalDate(
            year = year,
            month = monthNumber,
            day = dayOfMonth,
        )
    )

    constructor(isoString: String) : this(LocalDate.parse(isoString))

    override fun atTime(time: Time): DateTime {
        return KotlinxDateTime(
            year = year,
            monthNumber = monthNumber,
            dayOfMonth = dayOfMonth,
            hourOfDay = time.hourOfDay,
            minuteOfHour = time.minuteOfHour,
            secondOfMinute = time.secondOfMinute,
            millisOfSecond = time.millisOfSecond,
            nanosOfMilli = time.nanosOfMilli,
        )
    }

    override fun atStartOfDay(): DateTime {
        return KotlinxDateTime(
            year = year,
            monthNumber = monthNumber,
            dayOfMonth = dayOfMonth,
            hourOfDay = 0,
            minuteOfHour = 0,
            secondOfMinute = 0,
            millisOfSecond = 0,
            nanosOfMilli = 0,
        )
    }

    override fun atEndOfDay(): DateTime {
        return KotlinxDateTime(
            year = year,
            monthNumber = monthNumber,
            dayOfMonth = dayOfMonth,
            hourOfDay = 23,
            minuteOfHour = 59,
            secondOfMinute = 59,
            millisOfSecond = 999,
            nanosOfMilli = 999,
        )
    }

    override fun atStartOf(unit: DateUnit): Date {
        return when (unit) {
            DateUnit.DAY -> this
            DateUnit.WEEK -> {
                var date = this as Date
                val startOfWeek = week.first()
                while (date.dayOfWeek != startOfWeek) {
                    date = date.minus(1, DateUnit.DAY)
                }
                date
            }
            DateUnit.MONTH -> KotlinxDate(
                year = year,
                monthNumber = monthNumber,
                dayOfMonth = 1,
            )
            DateUnit.QUARTER -> KotlinxDate(
                year = year,
                monthNumber = (((monthNumber - 1) / 3) * 3) + 1,
                dayOfMonth = 1,
            )
            DateUnit.YEAR -> KotlinxDate(
                year = year,
                monthNumber = 1,
                dayOfMonth = 1,
            )
            DateUnit.CENTURY -> KotlinxDate(
                year = 100 * floor(year / 100.0).toInt(),
                monthNumber = 1,
                dayOfMonth = 1,
            )
        }
    }

    override fun minus(value: Int, unit: DateUnit): Date {
        return KotlinxDate(delegate.minus(value, unit.fromDomain()))
    }

    override fun plus(value: Int, unit: DateUnit): Date {
        return KotlinxDate(delegate.plus(value, unit.fromDomain()))
    }

    override fun daysBetween(date: Date): Int {
        return delegate.daysUntil(
            LocalDate(
                year = date.year,
                month = date.monthNumber,
                day = date.dayOfMonth,
            )
        )
    }

    override fun copy(year: Int, monthNumber: Int, dayOfMonth: Int): Date {
        return KotlinxDate(
            year = year,
            monthNumber = monthNumber,
            dayOfMonth = dayOfMonth,
        )
    }

    override fun equals(other: Any?): Boolean {
        return other is Date &&
            year == other.year &&
            monthNumber == other.monthNumber &&
            dayOfMonth == other.dayOfMonth
    }

    override fun hashCode(): Int {
        return year.hashCode().times(31) +
            monthNumber.hashCode().times(31) +
            dayOfMonth.hashCode().times(31)
    }

    override fun toString(): String {
        return delegate.toString()
    }

    override fun readObject(inputStream: ObjectInputStream) {
        delegate = LocalDate.fromEpochDays(inputStream.readLong().toInt())
    }

    override fun writeObject(outputStream: ObjectOutputStream) {
        outputStream.writeLong(delegate.toEpochDays())
    }
}