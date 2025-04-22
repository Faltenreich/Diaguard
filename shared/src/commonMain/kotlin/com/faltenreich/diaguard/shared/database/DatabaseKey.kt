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

    enum class MeasurementUnit(override val key: String) : DatabaseKey {

        BEATS_PER_MINUTE("beats_per_minute"),
        BREAD_UNITS("bread_units"),
        CARBOHYDRATES("carbohydrates"),
        CARBOHYDRATE_UNITS("carbohydrate_units"),
        INSULIN_UNITS("insulin_units"),
        KILOGRAMS("kilograms"),
        MILLIGRAMS_PER_DECILITER("milligrams_per_deciliter"),
        MILLIMETERS_OF_MERCURY("millimeters_of_mercury"),
        MILLIMOLES_PER_LITER("millimoles_per_liter"),
        MILLIMOLES_PER_MOLE("millimoles_per_mole"),
        MINUTES("minutes"),
        PERCENT("percent"),
        POUNDS("pounds");

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