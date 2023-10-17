package com.faltenreich.diaguard.backup.seed

import com.faltenreich.diaguard.MR

class SeedFactory {

    fun create(): List<SeedMeasurementProperty> = properties {
        property {
            key = "blood_sugar"
            name = MR.strings.blood_sugar
            icon = "\uD83E\uDE78"
            type {
                key = "blood_sugar"
                name = MR.strings.blood_sugar
                unit {
                    key = "milligrams_per_deciliter"
                    name = MR.strings.milligrams_per_deciliter
                    abbreviation = MR.strings.milligrams_per_deciliter_abbreviation
                    factor = 1.0
                }
                unit {
                    key = "millimoles_per_liter"
                    name = MR.strings.millimoles_per_liter
                    abbreviation = MR.strings.millimoles_per_liter_abbreviation
                    factor = 0.0555
                }
            }
        }
        property {
            key = "insulin"
            name = MR.strings.insulin
            icon = "\uD83D\uDC89"
            type {
                key = "bolus"
                name = MR.strings.bolus
                unit {
                    key = "insulin_units"
                    name = MR.strings.insulin_units
                    abbreviation = MR.strings.insulin_units_abbreviation
                    factor = 1.0
                }
            }
            type {
                key = "correction"
                name = MR.strings.correction
                unit {
                    key = "insulin_units"
                    name = MR.strings.insulin_units
                    abbreviation = MR.strings.insulin_units_abbreviation
                    factor = 1.0
                }
            }
            type {
                key = "basal"
                name = MR.strings.basal
                unit {
                    key = "insulin_units"
                    name = MR.strings.insulin_units
                    abbreviation = MR.strings.insulin_units_abbreviation
                    factor = 1.0
                }
            }
        }
        property {
            key = "meal"
            name = MR.strings.meal
            icon = "\uD83C\uDF5E"
            type {
                key = "meal"
                name = MR.strings.meal
                unit {
                    key = "carbohydrates"
                    name = MR.strings.carbohydrates
                    abbreviation = MR.strings.carbohydrates_abbreviation
                    factor = 1.0
                }
                unit {
                    key = "carbohydrate_units"
                    name = MR.strings.carbohydrate_units
                    abbreviation = MR.strings.carbohydrate_units_abbreviation
                    factor = 0.1
                }
                unit {
                    key = "bread_units"
                    name = MR.strings.bread_units
                    abbreviation = MR.strings.bread_units_abbreviation
                    factor = 0.0833
                }
            }
        }
        property {
            key = "activity"
            name = MR.strings.activity
            icon = "\uD83C\uDFC3"
            type {
                key = "activity"
                name = MR.strings.activity
                unit {
                    key = "minutes"
                    name = MR.strings.minutes
                    abbreviation = MR.strings.minutes_abbreviation
                    factor = 1.0
                }
            }
        }
        property {
            key = "hba1c"
            name = MR.strings.hba1c
            icon = "%"
            type {
                key = "hba1c"
                name = MR.strings.hba1c
                unit {
                    key = "percent"
                    name = MR.strings.percent
                    abbreviation = MR.strings.percent_abbreviation
                    factor = 1.0
                }
                unit {
                    key = "millimoles_per_mole"
                    name = MR.strings.millimoles_per_mole
                    abbreviation = MR.strings.millimoles_per_mole_abbreviation
                    factor = 0.00001
                }
            }
        }
        property {
            key = "weight"
            name = MR.strings.weight
            icon = "\uD83C\uDFCB"
            type {
                key = "weight"
                name = MR.strings.weight
                unit {
                    key = "kilograms"
                    name = MR.strings.kilograms
                    abbreviation = MR.strings.kilograms_abbreviation
                    factor = 1.0
                }
                unit {
                    key = "pounds"
                    name = MR.strings.pounds
                    abbreviation = MR.strings.pounds_abbreviation
                    factor = 2.20462262185
                }
            }
        }
        property {
            key = "pulse"
            name = MR.strings.pulse
            icon = "\uD83D\uDC9A"
            type {
                key = "pulse"
                name = MR.strings.pulse
                unit {
                    key = "beats_per_minute"
                    name = MR.strings.beats_per_minute
                    abbreviation = MR.strings.beats_per_minute_abbreviation
                    factor = 1.0
                }
            }
        }
        property {
            key = "blood_pressure"
            name = MR.strings.blood_pressure
            icon = "⛽"
            type {
                key = "systolic"
                name = MR.strings.systolic
                unit {
                    key = "millimeters_of_mercury"
                    name = MR.strings.millimeters_of_mercury
                    abbreviation = MR.strings.millimeters_of_mercury_abbreviation
                    factor = 1.0
                }
            }
            type {
                key = "diastolic"
                name = MR.strings.diastolic
                unit {
                    key = "millimeters_of_mercury"
                    name = MR.strings.millimeters_of_mercury
                    abbreviation = MR.strings.millimeters_of_mercury_abbreviation
                    factor = 1.0
                }
            }
        }
        property {
            key = "oxygen_saturation"
            name = MR.strings.oxygen_saturation
            icon = "O²"
            type {
                key = "oxygen_saturation"
                name = MR.strings.oxygen_saturation
                unit {
                    key = "percent"
                    name = MR.strings.percent
                    abbreviation = MR.strings.percent_abbreviation
                    factor = 1.0
                }
            }
        }
    }
}
