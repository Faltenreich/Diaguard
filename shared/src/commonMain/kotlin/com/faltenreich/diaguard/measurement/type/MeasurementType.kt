package com.faltenreich.diaguard.measurement.type

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
    val name: String,
    val key: String?,
    val sortIndex: Long,
    val selectedUnitId: Long?,
    val propertyId: Long,
) : DatabaseEntity {

    lateinit var property: MeasurementProperty
    lateinit var units: List<MeasurementUnit>

    var selectedUnit: MeasurementUnit? = null

    val isUserGenerated: Boolean
        get() = key == null

    val isPredefined: Boolean
        get() = !isUserGenerated

    object PredefinedKey {

        const val BLOOD_SUGAR = "blood_sugar"
        const val INSULIN_BOLUS = "insulin_bolus"
        const val INSULIN_CORRECTION = "insulin_correction"
        const val INSULIN_BASAL = "insulin_basal"
        const val MEAL = "meal"
        const val ACTIVITY = "activity"
        const val HBA1C = "hba1c"
        const val WEIGHT = "weight"
        const val PULSE = "pulse"
        const val BLOOD_PRESSURE_SYSTOLIC = "blood_pressure_systolic"
        const val BLOOD_PRESSURE_DIASTOLIC = "blood_pressure_diastolic"
        const val OXYGEN_SATURATION = "oxygen_saturation"
    }
}