package com.faltenreich.diaguard.navigation

import com.faltenreich.diaguard.datetime.DateTime

sealed interface NavigationTarget {

    data object Dashboard : NavigationTarget

    data class EntryForm(
        val entryId: Long = -1L,
        val dateTime: DateTime? = null,
        val foodId: Long = -1L,
    ) : NavigationTarget

    data class EntrySearch(val query: String = "") : NavigationTarget

    data object ExportForm : NavigationTarget

    data class FoodForm(val foodId: Long = -1L) : NavigationTarget

    data class FoodEatenList(val foodId: Long) : NavigationTarget

    data class FoodSearch(val mode: Mode) : NavigationTarget {

        enum class Mode {
            STROLL,
            FIND,
        }
    }

    data object FoodPreferenceList : NavigationTarget

    data object LicenseList : NavigationTarget

    data object Log : NavigationTarget

    data class MeasurementCategoryForm(val categoryId: Long = -1L) : NavigationTarget

    data object MeasurementCategoryList : NavigationTarget

    data class MeasurementPropertyForm(
        val categoryId: Long,
        val propertyId: Long? = -1L,
    ) : NavigationTarget

    data class MeasurementUnitList(val mode: Mode) : NavigationTarget {

        enum class Mode {
            STROLL,
            FIND,
        }
    }

    data object OverviewPreferenceList : NavigationTarget

    data object ReadBackupForm : NavigationTarget

    data object Statistic : NavigationTarget

    data class TagDetail(val tagId: Long) : NavigationTarget

    data object TagList : NavigationTarget

    data object Timeline : NavigationTarget

    data object WriteBackupForm : NavigationTarget
}