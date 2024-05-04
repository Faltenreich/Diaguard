package com.faltenreich.diaguard.measurement.property

enum class MeasurementAggregationStyle(val stableId: Int) {
    CUMULATIVE(stableId = 0),
    AVERAGE(stableId = 1),
    ;

    companion object {

        val DEFAULT = CUMULATIVE

        fun fromStableId(stableId: Int): MeasurementAggregationStyle {
            return entries.firstOrNull { it.stableId == stableId } ?: DEFAULT
        }
    }
}