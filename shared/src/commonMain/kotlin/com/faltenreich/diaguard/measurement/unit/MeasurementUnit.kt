package com.faltenreich.diaguard.measurement.unit

import com.faltenreich.diaguard.backup.seed.Seed
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
) : DatabaseEntity, Seed {

    lateinit var type: MeasurementType

    val isDefault: Boolean
        get() = factor == FACTOR_DEFAULT

    val isSelected: Boolean
        get() = type.selectedUnitId == id

    companion object {

        const val FACTOR_DEFAULT = 1.0
    }

    object Key {

        const val BLOOD_SUGAR_MG_DL = "blood_sugar_mg_dl"
        const val BLOOD_SUGAR_MMOL_L = "blood_sugar_mmol_l"
        const val INSULIN_BOLUS_IU = "insulin_bolus_iu"
        const val INSULIN_CORRECTION_IU = "insulin_correction_iu"
        const val INSULIN_BASAL_IU = "insulin_basal_iu"
        const val MEAL_CHO = "meal_cho"
        const val MEAL_CU = "meal_cu"
        const val MEAL_BU = "meal_bu"
        const val ACTIVITY_MINUTES = "activity_minutes"
        const val HBA1C_PERCENT = "hba1c_percent"
        const val HBA1C_MMOL_MOL = "hba1c_mmol_mol"
        const val WEIGHT_KG = "weight_kg"
        const val WEIGHT_LBS = "weight_lbs"
        const val PULSE_BPM = "pulse_bpm"
        const val BLOOD_PRESSURE_SYSTOLIC_MM_HG = "blood_pressure_systolic_mm_hg"
        const val BLOOD_PRESSURE_DIASTOLIC_MM_HG = "blood_pressure_diastolic_mm_hg"
        const val OXYGEN_SATURATION_PERCENT = "oxygen_saturation_percent"
    }
}