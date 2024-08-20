package com.faltenreich.diaguard.navigation.bottomsheet

import com.faltenreich.diaguard.navigation.screen.Screen

sealed interface BottomSheetNavigationIntent {

    data class NavigateTo(val screen: Screen, val popHistory: Boolean) : BottomSheetNavigationIntent
}