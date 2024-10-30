package com.faltenreich.diaguard.navigation.bottomsheet

import com.faltenreich.diaguard.navigation.screen.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class StateFlowBottomSheetNavigation : BottomSheetNavigation {

    private val _bottomSheet = MutableStateFlow<Screen?>(null)
    override val bottomSheet = _bottomSheet.asStateFlow()

    override fun openBottomSheet(bottomSheet: Screen) {
        _bottomSheet.tryEmit(bottomSheet)
    }

    override fun closeBottomSheet() {
        _bottomSheet.tryEmit(null)
    }
}