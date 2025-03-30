package com.faltenreich.diaguard.measurement.property

import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.aggregation_style_average
import diaguard.shared.generated.resources.aggregation_style_cumulative
import org.jetbrains.compose.resources.StringResource

enum class MeasurementAggregationStyle(
    val stableId: Int,
    val labelResource: StringResource,
) {
    CUMULATIVE(stableId = 0, labelResource = Res.string.aggregation_style_cumulative),
    AVERAGE(stableId = 1, labelResource = Res.string.aggregation_style_average),
    ;

    companion object {

        private val DEFAULT = CUMULATIVE

        fun fromStableId(stableId: Int): MeasurementAggregationStyle {
            return entries.firstOrNull { it.stableId == stableId } ?: DEFAULT
        }
    }
}