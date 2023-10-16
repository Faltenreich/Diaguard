package com.faltenreich.diaguard.measurement.property

import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.shared.database.DatabaseEntity
import com.faltenreich.diaguard.shared.datetime.DateTime

/**
 * Entity representing one medical property of the human body
 */
data class MeasurementProperty(
    override val id: Long,
    override val createdAt: DateTime,
    override val updatedAt: DateTime,
    val name: String,
    val key: String?,
    val icon: String?,
    val sortIndex: Long,
) : DatabaseEntity {

    lateinit var types: List<MeasurementType>

    val isUserGenerated: Boolean
        get() = key == null

    val isPredefined: Boolean
        get() = !isUserGenerated

    val isBloodSugar: Boolean
        get() = key == PredefinedKey.BLOOD_SUGAR

    companion object {

        // TODO: Replace with PredefinedKey.BLOOD_SUGAR
        const val BLOOD_SUGAR_ID = 1L
    }

    object PredefinedKey {

        const val BLOOD_SUGAR = "blood_sugar"
        const val INSULIN = "insulin"
        const val MEAL = "meal"
        const val ACTIVITY = "activity"
        const val HBA1C = "hba1c"
        const val WEIGHT = "weight"
        const val PULSE = "pulse"
        const val BLOOD_PRESSURE = "blood_pressure"
        const val OXYGEN_SATURATION = "oxygen_saturation"
    }
}