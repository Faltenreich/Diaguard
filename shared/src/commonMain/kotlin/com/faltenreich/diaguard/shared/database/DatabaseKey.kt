package com.faltenreich.diaguard.shared.database

object DatabaseKey {

    private const val DELIMITER = "."

    enum class MeasurementProperty(val key: String) {

        BLOOD_SUGAR("blood_sugar"),
        INSULIN("insulin"),
        MEAL("meal"),
        ACTIVITY("activity"),
        HBA1C("hba1c"),
        WEIGHT("weight"),
        PULSE("pulse"),
        BLOOD_PRESSURE("blood_pressure"),
        OXYGEN_SATURATION("oxygen_saturation"),
    }

    enum class MeasurementType(val id: String, property: MeasurementProperty) {

        BLOOD_SUGAR("blood_sugar", MeasurementProperty.BLOOD_SUGAR),
        INSULIN_BOLUS( "bolus", MeasurementProperty.INSULIN),
        INSULIN_CORRECTION( "correction", MeasurementProperty.INSULIN),
        INSULIN_BASAL("basal", MeasurementProperty.INSULIN),
        MEAL("meal", MeasurementProperty.MEAL),
        ACTIVITY("activity", MeasurementProperty.ACTIVITY),
        HBA1C("hba1c", MeasurementProperty.HBA1C),
        WEIGHT("weight", MeasurementProperty.WEIGHT),
        PULSE("pulse", MeasurementProperty.PULSE),
        BLOOD_PRESSURE_SYSTOLIC("systolic", MeasurementProperty.BLOOD_PRESSURE),
        BLOOD_PRESSURE_DIASTOLIC("diastolic", MeasurementProperty.BLOOD_PRESSURE),
        OXYGEN_SATURATION("oxygen_saturation", MeasurementProperty.OXYGEN_SATURATION),
        ;

        val key: String = property.key + DELIMITER + id
    }

    enum class MeasurementUnit(val id: String, type: MeasurementType) {

        BLOOD_SUGAR_MILLIGRAMS_PER_DECILITER("milligrams_per_deciliter", MeasurementType.BLOOD_SUGAR),
        BLOOD_SUGAR_MILLIMOLES_PER_LITER("millimoles_per_liter", MeasurementType.BLOOD_SUGAR),
        INSULIN_BOLUS("insulin_units", MeasurementType.INSULIN_BOLUS),
        INSULIN_CORRECTION("insulin_units", MeasurementType.INSULIN_CORRECTION),
        INSULIN_BASAL( "insulin_units", MeasurementType.INSULIN_BASAL),
        MEAL_CARBOHYDRATES("carbohydrates", MeasurementType.MEAL),
        MEAL_CARBOHYDRATE_UNITS("carbohydrate_units", MeasurementType.MEAL),
        MEAL_BREAD_UNITS("bread_units", MeasurementType.MEAL),
        ACTIVITY("minutes", MeasurementType.ACTIVITY),
        HBA1C_PERCENT("percent", MeasurementType.HBA1C),
        HBA1C_MILLIMOLES_PER_MOLES("millimoles_per_mole", MeasurementType.HBA1C),
        WEIGHT_KILOGRAMS("kilograms", MeasurementType.WEIGHT),
        WEIGHT_POUNDS("pounds", MeasurementType.WEIGHT),
        PULSE_BEATS_PER_MINUTE("beats_per_minute", MeasurementType.PULSE),
        BLOOD_PRESSURE_SYSTOLIC("systolic", MeasurementType.BLOOD_PRESSURE_SYSTOLIC),
        BLOOD_PRESSURE_DIASTOLIC("diastolic", MeasurementType.BLOOD_PRESSURE_DIASTOLIC),
        OXYGEN_SATURATION("oxygen_saturation", MeasurementType.OXYGEN_SATURATION),
        ;

        val key: String = type.key + DELIMITER + id
    }
}