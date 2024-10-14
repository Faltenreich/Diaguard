package com.faltenreich.diaguard.navigation.bottomsheet

import com.faltenreich.diaguard.navigation.screen.Screen

interface BottomSheetNavigation {

    fun openBottomSheet(bottomSheet: Screen)

    fun closeBottomSheet()
}