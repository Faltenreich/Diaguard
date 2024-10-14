package com.faltenreich.diaguard.main.menu

import com.faltenreich.diaguard.navigation.bottomsheet.CloseBottomSheetUseCase
import com.faltenreich.diaguard.navigation.screen.GetCurrentScreenUseCase
import com.faltenreich.diaguard.navigation.screen.NavigateToScreenUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.map

class MainMenuViewModel(
    getCurrentScreen: GetCurrentScreenUseCase = inject(),
    private val navigateToScreen: NavigateToScreenUseCase = inject(),
    private val closeBottomSheet: CloseBottomSheetUseCase = inject(),
) : ViewModel<MainMenuState, MainMenuIntent, Unit>() {

    override val state = getCurrentScreen().map(::MainMenuState)

    override suspend fun handleIntent(intent: MainMenuIntent) = with(intent) {
        when (this) {
            is MainMenuIntent.NavigateTo -> {
                closeBottomSheet()
                // Attention: Must be executed at last to not break FoodSearchMode
                navigateToScreen(screen, popHistory)
            }
        }
    }
}