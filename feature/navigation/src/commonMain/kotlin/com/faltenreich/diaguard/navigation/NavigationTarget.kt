package com.faltenreich.diaguard.navigation

sealed interface NavigationTarget {

    data object FoodPreferenceList : NavigationTarget

    data object LicenseList : NavigationTarget

    data object MeasurementCategoryList : NavigationTarget

    data class MeasurementUnitList(val mode: Mode) : NavigationTarget {

        enum class Mode {
            STROLL,
            FIND,
        }
    }

    data object ReadBackupForm : NavigationTarget

    data object TagList : NavigationTarget

    data object WriteBackupForm : NavigationTarget
}