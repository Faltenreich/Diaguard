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

    enum class MeasurementProperty(val id: String, category: MeasurementCategory) : DatabaseKey {

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

            fun from(key: String): MeasurementProperty {
                return entries.first { it.key == key }
            }
        }
    }

    enum class MeasurementUnit(val id: String, property: MeasurementProperty) : DatabaseKey {

        BLOOD_SUGAR_MILLIGRAMS_PER_DECILITER("milligrams_per_deciliter", MeasurementProperty.BLOOD_SUGAR),
        BLOOD_SUGAR_MILLIMOLES_PER_LITER("millimoles_per_liter", MeasurementProperty.BLOOD_SUGAR),
        INSULIN_BOLUS("insulin_units", MeasurementProperty.INSULIN_BOLUS),
        INSULIN_CORRECTION("insulin_units", MeasurementProperty.INSULIN_CORRECTION),
        INSULIN_BASAL( "insulin_units", MeasurementProperty.INSULIN_BASAL),
        MEAL_CARBOHYDRATES("carbohydrates", MeasurementProperty.MEAL),
        MEAL_CARBOHYDRATE_UNITS("carbohydrate_units", MeasurementProperty.MEAL),
        MEAL_BREAD_UNITS("bread_units", MeasurementProperty.MEAL),
        ACTIVITY("minutes", MeasurementProperty.ACTIVITY),
        HBA1C_PERCENT("percent", MeasurementProperty.HBA1C),
        HBA1C_MILLIMOLES_PER_MOLES("millimoles_per_mole", MeasurementProperty.HBA1C),
        WEIGHT_KILOGRAMS("kilograms", MeasurementProperty.WEIGHT),
        WEIGHT_POUNDS("pounds", MeasurementProperty.WEIGHT),
        PULSE("beats_per_minute", MeasurementProperty.PULSE),
        BLOOD_PRESSURE_SYSTOLIC("millimeters_of_mercury", MeasurementProperty.BLOOD_PRESSURE_SYSTOLIC),
        BLOOD_PRESSURE_DIASTOLIC("millimeters_of_mercury", MeasurementProperty.BLOOD_PRESSURE_DIASTOLIC),
        OXYGEN_SATURATION("percent", MeasurementProperty.OXYGEN_SATURATION),
        ;

        override val key: String = property.key + DELIMITER + id

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