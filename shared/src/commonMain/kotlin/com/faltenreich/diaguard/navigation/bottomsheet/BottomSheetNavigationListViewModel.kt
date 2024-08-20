package com.faltenreich.diaguard.navigation.bottomsheet

import com.faltenreich.diaguard.navigation.NavigateToScreenUseCase
import com.faltenreich.diaguard.navigation.screen.GetActiveScreenUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.map

class BottomSheetNavigationListViewModel(
    getActiveScreen: GetActiveScreenUseCase = inject(),
    private val navigateToScreen: NavigateToScreenUseCase = inject(),
    private val closeBottomSheet: CloseBottomSheetUseCase = inject(),
) : ViewModel<BottomSheetNavigationListState, BottomSheetNavigationListIntent, Unit>() {

    override val state = getActiveScreen().map(::BottomSheetNavigationListState)

    override suspend fun handleIntent(intent: BottomSheetNavigationListIntent) = with(intent) {
        when (this) {
            is BottomSheetNavigationListIntent.NavigateTo -> {
                navigateToScreen(screen, popHistory)
                closeBottomSheet()
            }
        }
    }
}