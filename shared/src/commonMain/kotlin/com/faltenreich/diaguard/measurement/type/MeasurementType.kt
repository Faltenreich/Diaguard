package com.faltenreich.diaguard.measurement.type

import com.faltenreich.diaguard.backup.seed.Seedable
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.shared.database.DatabaseEntity
import com.faltenreich.diaguard.shared.datetime.DateTime

/**
 * Entity representing one type of [MeasurementProperty]
 *
 * TODO: Possible and target ranges
 */
data class MeasurementType(
    override val id: Long,
    override val createdAt: DateTime,
    override val updatedAt: DateTime,
    override val key: String?,
    val name: String,
    val sortIndex: Long,
    val selectedUnitId: Long?,
    val propertyId: Long,
) : DatabaseEntity, Seedable {

    lateinit var property: MeasurementProperty
    lateinit var units: List<MeasurementUnit>

    var selectedUnit: MeasurementUnit? = null

    object Key {

        const val BLOOD_SUGAR = MeasurementProperty.Key.BLOOD_SUGAR
        const val INSULIN_BOLUS = "${MeasurementProperty.Key.INSULIN}_bolus"
        const val INSULIN_CORRECTION = "${MeasurementProperty.Key.INSULIN}_correction"
        const val INSULIN_BASAL = "${MeasurementProperty.Key.INSULIN}_basal"
        const val MEAL = MeasurementProperty.Key.MEAL
        const val ACTIVITY = MeasurementProperty.Key.ACTIVITY
        const val HBA1C = MeasurementProperty.Key.HBA1C
        const val WEIGHT = MeasurementProperty.Key.WEIGHT
        const val PULSE = MeasurementProperty.Key.PULSE
        const val BLOOD_PRESSURE_SYSTOLIC = "${MeasurementProperty.Key.BLOOD_PRESSURE}_systolic"
        const val BLOOD_PRESSURE_DIASTOLIC = "${MeasurementProperty.Key.BLOOD_PRESSURE}_diastolic"
        const val OXYGEN_SATURATION = MeasurementProperty.Key.OXYGEN_SATURATION
    }
}