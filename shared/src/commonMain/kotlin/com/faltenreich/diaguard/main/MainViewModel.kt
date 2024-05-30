package com.faltenreich.diaguard.main

import com.faltenreich.diaguard.backup.HasDataUseCase
import com.faltenreich.diaguard.backup.ImportUseCase
import com.faltenreich.diaguard.navigation.GetActiveScreenUseCase
import com.faltenreich.diaguard.navigation.GetModalUseCase
import com.faltenreich.diaguard.navigation.NavigateBackUseCase
import com.faltenreich.diaguard.navigation.NavigateToScreenUseCase
import com.faltenreich.diaguard.navigation.NavigationIntent
import com.faltenreich.diaguard.navigation.screen.DashboardScreen
import com.faltenreich.diaguard.navigation.screen.LogScreen
import com.faltenreich.diaguard.navigation.screen.TimelineScreen
import com.faltenreich.diaguard.preference.StartScreen
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class MainViewModel(
    getPreference: GetPreferenceUseCase = inject(),
    getModal: GetModalUseCase = inject(),
    private val hasData: HasDataUseCase = inject(),
    private val importData: ImportUseCase = inject(),
    private val navigateToScreen: NavigateToScreenUseCase = inject(),
    val getActiveScreen: GetActiveScreenUseCase = inject(),
    private val navigateBack: NavigateBackUseCase = inject(),
) : ViewModel<MainState, NavigationIntent, Unit>() {

    override val state = combine(
        hasData(),
        getPreference(StartScreen.Preference),
        getModal(),
    ) { hasData, startScreen, modal ->
        if (hasData) {
            MainState.Loaded(
                startScreen = when (startScreen) {
                    StartScreen.DASHBOARD -> DashboardScreen
                    StartScreen.TIMELINE -> TimelineScreen
                    StartScreen.LOG -> LogScreen
                },
                modal = modal,
            )
        } else {
            MainState.Loading
        }
    }

    init {
        scope.launch {
            // TODO: Check if this works as intended
            hasData().collect { hasData ->
                if (!hasData) {
                    importData()
                }
            }
        }
    }

    override suspend fun handleIntent(intent: NavigationIntent) {
        when (intent) {
            is NavigationIntent.NavigateTo -> navigateToScreen(intent.screen)
            is NavigationIntent.NavigateBack -> navigateBack()
        }
    }
}