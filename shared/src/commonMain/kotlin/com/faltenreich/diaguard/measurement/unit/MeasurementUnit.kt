package com.faltenreich.diaguard.measurement.unit

import com.faltenreich.diaguard.backup.seed.Seedable
import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.shared.database.DatabaseEntity
import com.faltenreich.diaguard.shared.datetime.DateTime

/**
 * Entity representing the unit of a [MeasurementType]
 */
data class MeasurementUnit(
    override val id: Long,
    override val createdAt: DateTime,
    override val updatedAt: DateTime,
    override val key: String?,
    val name: String,
    val factor: Double,
    val typeId: Long,
) : DatabaseEntity, Seedable {

    lateinit var type: MeasurementType

    val isDefault: Boolean
        get() = factor == FACTOR_DEFAULT

    val isSelected: Boolean
        get() = type.selectedUnitId == id

    companion object {

        const val FACTOR_DEFAULT = 1.0
    }

    object Key {

        const val BLOOD_SUGAR_MG_DL = "${MeasurementType.Key.BLOOD_SUGAR}_mg_dl"
        const val BLOOD_SUGAR_MMOL_L = "${MeasurementType.Key.BLOOD_SUGAR}_mmol_l"
        const val INSULIN_BOLUS_IU = "${MeasurementType.Key.INSULIN_BOLUS}_iu"
        const val INSULIN_CORRECTION_IU = "${MeasurementType.Key.INSULIN_CORRECTION}_iu"
        const val INSULIN_BASAL_IU = "${MeasurementType.Key.INSULIN_BASAL}_iu"
        const val MEAL_CHO = "${MeasurementType.Key.MEAL}_cho"
        const val MEAL_CU = "${MeasurementType.Key.MEAL}_cu"
        const val MEAL_BU = "${MeasurementType.Key.MEAL}_bu"
        const val ACTIVITY_MINUTES = "${MeasurementType.Key.ACTIVITY}_minutes"
        const val HBA1C_PERCENT = "${MeasurementType.Key.HBA1C}_percent"
        const val HBA1C_MMOL_MOL = "${MeasurementType.Key.HBA1C}_mmol_mol"
        const val WEIGHT_KG = "${MeasurementType.Key.WEIGHT}_kg"
        const val WEIGHT_LBS = "${MeasurementType.Key.WEIGHT}_lbs"
        const val PULSE_BPM = "${MeasurementType.Key.PULSE}_bpm"
        const val BLOOD_PRESSURE_SYSTOLIC_MM_HG = "${MeasurementType.Key.BLOOD_PRESSURE_SYSTOLIC}_mm_hg"
        const val BLOOD_PRESSURE_DIASTOLIC_MM_HG = "${MeasurementType.Key.BLOOD_PRESSURE_DIASTOLIC}_mm_hg"
        const val OXYGEN_SATURATION_PERCENT = "${MeasurementType.Key.OXYGEN_SATURATION}_percent"
    }
}