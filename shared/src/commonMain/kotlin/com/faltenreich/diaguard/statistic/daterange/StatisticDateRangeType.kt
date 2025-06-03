package com.faltenreich.diaguard.statistic.daterange

import com.faltenreich.diaguard.datetime.DateUnit
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.month
import diaguard.shared.generated.resources.quarter
import diaguard.shared.generated.resources.week
import diaguard.shared.generated.resources.year
import org.jetbrains.compose.resources.StringResource

enum class StatisticDateRangeType(
    val intervalDateUnit: DateUnit,
    val labelResource: StringResource,
) {

    WEEK(
        intervalDateUnit = DateUnit.DAY,
        labelResource = Res.string.week,
    ),
    MONTH(
        intervalDateUnit = DateUnit.WEEK,
        labelResource = Res.string.month,
    ),
    QUARTER(
        intervalDateUnit = DateUnit.MONTH,
        labelResource = Res.string.quarter,
    ),
    YEAR(
        intervalDateUnit = DateUnit.QUARTER,
        labelResource = Res.string.year,
    ),
}