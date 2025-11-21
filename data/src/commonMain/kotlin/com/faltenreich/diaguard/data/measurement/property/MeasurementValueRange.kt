package com.faltenreich.diaguard.data.measurement.property

data class MeasurementValueRange(
    val minimum: Double,
    val low: Double?,
    val target: Double?,
    val high: Double?,
    val maximum: Double,
    val isHighlighted: Boolean,
) {

    companion object {

        const val BLOOD_SUGAR_TARGET_DEFAULT = 120.0
    }
}