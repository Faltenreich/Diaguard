package com.faltenreich.diaguard.shared.database

interface DatabaseKey {

    // Attention: Keys must not be altered as they will be engraved into the seed database
    val key: String

    enum class MeasurementCategory(override val key: String) : DatabaseKey {

        BLOOD_SUGAR("blood_sugar"),
        INSULIN("insulin"),
        MEAL("meal"),
        ACTIVITY("activity"),
        HBA1C("hba1c"),
        WEIGHT("weight"),
        PULSE("pulse"),
        BLOOD_PRESSURE("blood_pressure"),
        OXYGEN_SATURATION("oxygen_saturation"),
        ;

        companion object {

            fun from(key: String): MeasurementCategory {
                return entries.first { it.key == key }
            }
        }
    }

    enum class MeasurementType(val id: String, category: MeasurementCategory) : DatabaseKey {

        BLOOD_SUGAR("blood_sugar", MeasurementCategory.BLOOD_SUGAR),
        INSULIN_BOLUS( "bolus", MeasurementCategory.INSULIN),
        INSULIN_CORRECTION( "correction", MeasurementCategory.INSULIN),
        INSULIN_BASAL("basal", MeasurementCategory.INSULIN),
        MEAL("meal", MeasurementCategory.MEAL),
        ACTIVITY("activity", MeasurementCategory.ACTIVITY),
        HBA1C("hba1c", MeasurementCategory.HBA1C),
        WEIGHT("weight", MeasurementCategory.WEIGHT),
        PULSE("pulse", MeasurementCategory.PULSE),
        BLOOD_PRESSURE_SYSTOLIC("systolic", MeasurementCategory.BLOOD_PRESSURE),
        BLOOD_PRESSURE_DIASTOLIC("diastolic", MeasurementCategory.BLOOD_PRESSURE),
        OXYGEN_SATURATION("oxygen_saturation", MeasurementCategory.OXYGEN_SATURATION),
        ;

        override val key: String = category.key + DELIMITER + id

        companion object {

            fun from(key: String): MeasurementType {
                return entries.first { it.key == key }
            }
        }
    }

    enum class MeasurementUnit(val id: String, type: MeasurementType) : DatabaseKey {

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
        PULSE("beats_per_minute", MeasurementType.PULSE),
        BLOOD_PRESSURE_SYSTOLIC("millimeters_of_mercury", MeasurementType.BLOOD_PRESSURE_SYSTOLIC),
        BLOOD_PRESSURE_DIASTOLIC("millimeters_of_mercury", MeasurementType.BLOOD_PRESSURE_DIASTOLIC),
        OXYGEN_SATURATION("percent", MeasurementType.OXYGEN_SATURATION),
        ;

        override val key: String = type.key + DELIMITER + id

        companion object {

            fun from(key: String): MeasurementUnit {
                return entries.first { it.key == key }
            }
        }
    }

    companion object {

        private const val DELIMITER = "."
    }
}