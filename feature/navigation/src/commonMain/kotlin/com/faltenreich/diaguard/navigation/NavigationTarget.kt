package com.faltenreich.diaguard.navigation

sealed interface NavigationTarget {

    data object LicenseList : NavigationTarget
}