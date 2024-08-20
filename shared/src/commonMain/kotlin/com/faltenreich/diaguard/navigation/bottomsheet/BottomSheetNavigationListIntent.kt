package com.faltenreich.diaguard.navigation.bottomsheet

import com.faltenreich.diaguard.navigation.screen.Screen

sealed interface BottomSheetNavigationListIntent {

    data class NavigateTo(val screen: Screen, val popHistory: Boolean) : BottomSheetNavigationListIntent
}