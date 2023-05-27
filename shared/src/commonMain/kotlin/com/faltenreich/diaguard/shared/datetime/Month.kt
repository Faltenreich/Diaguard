package com.faltenreich.diaguard.shared.datetime

import com.faltenreich.diaguard.MR
import dev.icerock.moko.resources.StringResource

enum class Month(
    val monthOfYear: Int,
    val label: StringResource,
    val abbreviation: StringResource,
) {
    JANUARY(
        monthOfYear = 1,
        label = MR.strings.month_january,
        abbreviation = MR.strings.month_january_short,
    ),
    FEBRUARY(
        monthOfYear = 2,
        label = MR.strings.month_february,
        abbreviation = MR.strings.month_february_short,
    ),
    MARCH(
        monthOfYear = 3,
        label = MR.strings.month_march,
        abbreviation = MR.strings.month_march_short,
    ),
    APRIL(
        monthOfYear = 4,
        label = MR.strings.month_april,
        abbreviation = MR.strings.month_april_short,
    ),
    MAY(
        monthOfYear = 5,
        label = MR.strings.month_may,
        abbreviation = MR.strings.month_may_short,
    ),
    JUNE(
        monthOfYear = 6,
        label = MR.strings.month_june,
        abbreviation = MR.strings.month_june_short,
    ),
    JULY(
        monthOfYear = 7,
        label = MR.strings.month_july,
        abbreviation = MR.strings.month_july_short,
    ),
    AUGUST(
        monthOfYear = 8,
        label = MR.strings.month_august,
        abbreviation = MR.strings.month_august_short,
    ),
    SEPTEMBER(
        monthOfYear = 9,
        label = MR.strings.month_september,
        abbreviation = MR.strings.month_september_short,
    ),
    OCTOBER(
        monthOfYear = 10,
        label = MR.strings.month_october,
        abbreviation = MR.strings.month_october_short,
    ),
    NOVEMBER(
        monthOfYear = 11,
        label = MR.strings.month_november,
        abbreviation = MR.strings.month_november_short,
    ),
    DECEMBER(
        monthOfYear = 12,
        label = MR.strings.month_december,
        abbreviation = MR.strings.month_december_short,
    ),
    ;

    companion object {

        fun fromMonthOfYear(monthOfYear: Int): Month {
            return values().first { it.monthOfYear == monthOfYear }
        }
    }
}