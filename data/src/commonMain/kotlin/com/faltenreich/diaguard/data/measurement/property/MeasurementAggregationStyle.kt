package com.faltenreich.diaguard.data.measurement.property

import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.aggregation_style_average
import diaguard.shared.generated.resources.aggregation_style_average_description
import diaguard.shared.generated.resources.aggregation_style_cumulative
import diaguard.shared.generated.resources.aggregation_style_cumulative_description
import org.jetbrains.compose.resources.StringResource

enum class MeasurementAggregationStyle(
    val stableId: Int,
    val labelResource: StringResource,
    val descriptionResource: StringResource,
) {
    CUMULATIVE(
        stableId = 0,
        labelResource = Res.string.aggregation_style_cumulative,
        descriptionResource = Res.string.aggregation_style_cumulative_description,
    ),
    AVERAGE(
        stableId = 1,
        labelResource = Res.string.aggregation_style_average,
        descriptionResource = Res.string.aggregation_style_average_description,
    ),
    ;

    companion object {

        private val DEFAULT = CUMULATIVE

        fun fromStableId(stableId: Int): MeasurementAggregationStyle {
            return entries.firstOrNull { it.stableId == stableId } ?: DEFAULT
        }
    }
}