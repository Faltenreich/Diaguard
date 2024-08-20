package com.faltenreich.diaguard.main.menu

import com.faltenreich.diaguard.navigation.screen.NavigateToScreenUseCase
import com.faltenreich.diaguard.navigation.bottomsheet.CloseBottomSheetUseCase
import com.faltenreich.diaguard.navigation.screen.GetActiveScreenUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.map

class MainMenuViewModel(
    getActiveScreen: GetActiveScreenUseCase = inject(),
    private val navigateToScreen: NavigateToScreenUseCase = inject(),
    private val closeBottomSheet: CloseBottomSheetUseCase = inject(),
) : ViewModel<MainMenuState, MainMenuIntent, Unit>() {

    override val state = getActiveScreen().map(::MainMenuState)

    override suspend fun handleIntent(intent: MainMenuIntent) = with(intent) {
        when (this) {
            is MainMenuIntent.NavigateTo -> {
                navigateToScreen(screen, popHistory)
                closeBottomSheet()
            }
        }
    }
}