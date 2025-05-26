package com.faltenreich.diaguard.statistic.daterange

import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.custom
import diaguard.shared.generated.resources.day
import diaguard.shared.generated.resources.quarter
import diaguard.shared.generated.resources.week
import diaguard.shared.generated.resources.year
import org.jetbrains.compose.resources.StringResource

enum class StatisticDateRangeType(val labelResource: StringResource) {

    DAY(Res.string.day),
    WEEK(Res.string.week),
    QUARTER(Res.string.quarter),
    YEAR(Res.string.year),
    CUSTOM(Res.string.custom),
}