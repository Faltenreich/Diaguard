package com.faltenreich.diaguard.main.menu

import com.faltenreich.diaguard.navigation.bottomsheet.CloseBottomSheetUseCase
import com.faltenreich.diaguard.navigation.screen.PushScreenUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import kotlinx.coroutines.flow.flowOf

class MainMenuViewModel(
    currentDestination: String?,
    private val pushScreen: PushScreenUseCase,
    private val closeBottomSheet: CloseBottomSheetUseCase,
) : ViewModel<MainMenuState, MainMenuIntent, Unit>() {

    override val state = flowOf(MainMenuState(currentDestination))

    override suspend fun handleIntent(intent: MainMenuIntent) = with(intent) {
        when (this) {
            is MainMenuIntent.PushScreen -> {
                closeBottomSheet()
                pushScreen(screen, popHistory)
            }
        }
    }
}