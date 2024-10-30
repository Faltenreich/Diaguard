package com.faltenreich.diaguard.navigation.bottomsheet

import com.faltenreich.diaguard.navigation.screen.Screen
import kotlinx.coroutines.flow.StateFlow

interface BottomSheetNavigation {

    val bottomSheet: StateFlow<Screen?>

    fun openBottomSheet(bottomSheet: Screen)

    fun closeBottomSheet()
}