package com.faltenreich.diaguard.navigation

import com.faltenreich.diaguard.navigation.screen.DashboardScreen
import com.faltenreich.diaguard.navigation.screen.LogScreen
import com.faltenreich.diaguard.navigation.screen.TimelineScreen
import com.faltenreich.diaguard.preference.StartScreenPreference
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.preference.store.StartScreen
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.map

class NavigationViewModel(
    getPreference: GetPreferenceUseCase = inject(),
    private val navigateToScreen: NavigateToScreenUseCase = inject(),
    val getActiveScreen: GetActiveScreenUseCase = inject(),
    private val navigateBack: NavigateBackUseCase = inject(),
    val canNavigateBack: CanNavigateBackUseCase = inject(),
) : ViewModel<NavigationViewState, NavigationIntent>() {

    override val state = getPreference(StartScreenPreference).map { startScreen ->
        NavigationViewState(
            startScreen = when (startScreen) {
                StartScreen.DASHBOARD -> DashboardScreen
                StartScreen.TIMELINE -> TimelineScreen()
                StartScreen.LOG -> LogScreen()
            },
        )
    }

    override fun onIntent(intent: NavigationIntent) {
        when (intent) {
            is NavigationIntent.NavigateTo -> navigateToScreen(intent.screen)
            is NavigationIntent.NavigateBack -> navigateBack()
        }
    }
}