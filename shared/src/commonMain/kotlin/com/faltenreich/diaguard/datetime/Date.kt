package com.faltenreich.diaguard.datetime

import com.faltenreich.diaguard.datetime.kotlinx.KotlinxDate
import com.faltenreich.diaguard.shared.serialization.ObjectInputStream
import com.faltenreich.diaguard.shared.serialization.ObjectOutputStream
import com.faltenreich.diaguard.shared.serialization.Serializable

interface Date : Serializable, Comparable<Date> {

    /**
     * Year starting at 0 AD
     */
    val year: Int

    /**
     * Month-of-year as number ranging from 1 to 12
     */
    val monthNumber: Int

    /**
     * Month ranging from January to December
     */
    val month: Month
        get() = Month.fromMonthNumber(monthNumber)

    /**
     * Month-of-year as pair of Month and year
     */
    val monthOfYear: MonthOfYear
        get() = MonthOfYear(month, year)

    /**
     * Day-of-month ranging from 1 to 31, depending on month
     */
    val dayOfMonth: Int

    /**
     * Day-of-week ranging from start- to end of week, depending on the locale
     */
    val dayOfWeek: DayOfWeek

    fun atTime(time: Time): DateTime

    fun atStartOfDay(): DateTime

    fun atEndOfDay(): DateTime

    fun minus(value: Int, unit: DateUnit): Date

    fun plus(value: Int, unit: DateUnit): Date

    fun daysBetween(date: Date): Int

    //region Any

    fun copy(
        year: Int = this.year,
        monthNumber: Int = this.monthNumber,
        dayOfMonth: Int = this.dayOfMonth,
    ): Date = Date(
        year = year,
        monthNumber = monthNumber,
        dayOfMonth = dayOfMonth,
    )

    override fun equals(other: Any?): Boolean

    override fun hashCode(): Int

    override fun toString(): String

    //endregion Any

    //region Serializable

    fun readObject(inputStream: ObjectInputStream)

    fun writeObject(outputStream: ObjectOutputStream)

    //endregion Serializable

    //region Comparable

    override fun compareTo(other: Date): Int {
        return when {
            year > other.year -> 1
            year < other.year -> -1
            monthNumber > other.monthNumber -> 1
            monthNumber < other.monthNumber -> -1
            dayOfMonth > other.dayOfMonth -> 1
            dayOfMonth < other.dayOfMonth -> -1
            else -> 0
        }
    }

    operator fun rangeTo(that: Date): DateRange = DateRange(this, that)

    //endregion Comparable

    companion object {

        operator fun invoke(
            year: Int,
            monthNumber: Int,
            dayOfMonth: Int,
        ): Date = KotlinxDate(
            year,
            monthNumber,
            dayOfMonth,
        )

        operator fun invoke(isoString: String): Date = KotlinxDate(isoString)

        fun today(): Date = DateTime.now().date
    }
}