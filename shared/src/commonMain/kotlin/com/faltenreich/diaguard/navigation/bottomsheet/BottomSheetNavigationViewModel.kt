package com.faltenreich.diaguard.navigation.bottomsheet

import com.faltenreich.diaguard.navigation.NavigateToScreenUseCase
import com.faltenreich.diaguard.navigation.screen.GetActiveScreenUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.map

class BottomSheetNavigationViewModel(
    getActiveScreen: GetActiveScreenUseCase = inject(),
    private val navigateToScreen: NavigateToScreenUseCase = inject(),
    private val closeBottomSheet: CloseBottomSheetUseCase = inject(),
) : ViewModel<BottomSheetNavigationState, BottomSheetNavigationIntent, Unit>() {

    override val state = getActiveScreen().map(::BottomSheetNavigationState)

    override suspend fun handleIntent(intent: BottomSheetNavigationIntent) = with(intent) {
        when (this) {
            is BottomSheetNavigationIntent.NavigateTo -> {
                navigateToScreen(screen)
                closeBottomSheet()
            }
        }
    }
}