package com.faltenreich.diaguard.datetime

import diaguard.shared.generated.resources.*
import org.jetbrains.compose.resources.StringResource

enum class Month(
    val monthNumber: Int,
    val label: StringResource,
    val abbreviation: StringResource,
) {
    JANUARY(
        monthNumber = 1,
        label = Res.string.month_january,
        abbreviation = Res.string.month_january_short,
    ),
    FEBRUARY(
        monthNumber = 2,
        label = Res.string.month_february,
        abbreviation = Res.string.month_february_short,
    ),
    MARCH(
        monthNumber = 3,
        label = Res.string.month_march,
        abbreviation = Res.string.month_march_short,
    ),
    APRIL(
        monthNumber = 4,
        label = Res.string.month_april,
        abbreviation = Res.string.month_april_short,
    ),
    MAY(
        monthNumber = 5,
        label = Res.string.month_may,
        abbreviation = Res.string.month_may_short,
    ),
    JUNE(
        monthNumber = 6,
        label = Res.string.month_june,
        abbreviation = Res.string.month_june_short,
    ),
    JULY(
        monthNumber = 7,
        label = Res.string.month_july,
        abbreviation = Res.string.month_july_short,
    ),
    AUGUST(
        monthNumber = 8,
        label = Res.string.month_august,
        abbreviation = Res.string.month_august_short,
    ),
    SEPTEMBER(
        monthNumber = 9,
        label = Res.string.month_september,
        abbreviation = Res.string.month_september_short,
    ),
    OCTOBER(
        monthNumber = 10,
        label = Res.string.month_october,
        abbreviation = Res.string.month_october_short,
    ),
    NOVEMBER(
        monthNumber = 11,
        label = Res.string.month_november,
        abbreviation = Res.string.month_november_short,
    ),
    DECEMBER(
        monthNumber = 12,
        label = Res.string.month_december,
        abbreviation = Res.string.month_december_short,
    ),
    ;

    companion object {

        fun fromMonthNumber(monthNumber: Int): Month {
            return entries.first { it.monthNumber == monthNumber }
        }
    }
}