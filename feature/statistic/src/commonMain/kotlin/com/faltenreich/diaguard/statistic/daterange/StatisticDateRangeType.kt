package com.faltenreich.diaguard.statistic.daterange

import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.month
import com.faltenreich.diaguard.resource.quarter
import com.faltenreich.diaguard.resource.week
import com.faltenreich.diaguard.resource.year
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
        intervalDateUnit = DateUnit.MONTH,
        labelResource = Res.string.year,
    ),
}