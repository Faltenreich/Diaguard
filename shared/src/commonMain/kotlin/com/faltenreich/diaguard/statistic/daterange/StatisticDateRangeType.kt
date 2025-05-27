package com.faltenreich.diaguard.statistic.daterange

import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.month
import diaguard.shared.generated.resources.quarter
import diaguard.shared.generated.resources.week
import diaguard.shared.generated.resources.year
import org.jetbrains.compose.resources.StringResource

enum class StatisticDateRangeType(val labelResource: StringResource) {

    WEEK(Res.string.week),
    MONTH(Res.string.month),
    QUARTER(Res.string.quarter),
    YEAR(Res.string.year),
}