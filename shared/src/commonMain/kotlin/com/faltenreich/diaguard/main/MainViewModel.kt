package com.faltenreich.diaguard.main

import com.faltenreich.diaguard.backup.ImportUseCase
import com.faltenreich.diaguard.dashboard.DashboardScreen
import com.faltenreich.diaguard.log.LogScreen
import com.faltenreich.diaguard.navigation.CloseBottomSheetUseCase
import com.faltenreich.diaguard.navigation.GetActiveScreenUseCase
import com.faltenreich.diaguard.navigation.GetCurrentScreenUseCase
import com.faltenreich.diaguard.navigation.GetModalUseCase
import com.faltenreich.diaguard.navigation.NavigateBackUseCase
import com.faltenreich.diaguard.navigation.NavigateToScreenUseCase
import com.faltenreich.diaguard.navigation.NavigationIntent
import com.faltenreich.diaguard.navigation.OpenBottomSheetUseCase
import com.faltenreich.diaguard.navigation.bottom.GetBottomSheetUseCase
import com.faltenreich.diaguard.preference.StartScreen
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.timeline.TimelineScreen
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class MainViewModel(
    getPreference: GetPreferenceUseCase = inject(),
    getCurrentScreen: GetCurrentScreenUseCase = inject(),
    getBottomSheet: GetBottomSheetUseCase = inject(),
    getModal: GetModalUseCase = inject(),
    private val hasData: HasDataUseCase = inject(),
    private val importData: ImportUseCase = inject(),
    private val navigateToScreen: NavigateToScreenUseCase = inject(),
    // TODO: Make private and expose via state
    val getActiveScreen: GetActiveScreenUseCase = inject(),
    private val navigateBack: NavigateBackUseCase = inject(),
    private val openBottomSheet: OpenBottomSheetUseCase = inject(),
    private val closeBottomSheet: CloseBottomSheetUseCase = inject(),
) : ViewModel<MainState, NavigationIntent, Unit>() {

    override val state = combine(
        hasData(),
        getPreference(StartScreen.Preference),
        getCurrentScreen(),
        getBottomSheet(),
        getModal(),
    ) { hasData, startScreen, currentScreen, bottomSheet, modal ->
        if (hasData) {
            MainState.Loaded(
                startScreen = when (startScreen) {
                    StartScreen.DASHBOARD -> DashboardScreen
                    StartScreen.TIMELINE -> TimelineScreen
                    StartScreen.LOG -> LogScreen
                },
                currentScreen = currentScreen,
                bottomSheet = bottomSheet,
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
                // FIXME: Violates unique constraints if executed redundantly
                if (!hasData) {
                    importData()
                }
            }
        }
    }

    override suspend fun handleIntent(intent: NavigationIntent) = with(intent) {
        when (this) {
            is NavigationIntent.NavigateTo -> {
                navigateToScreen(screen)
                closeBottomSheet()
            }
            is NavigationIntent.NavigateBack -> navigateBack()
            is NavigationIntent.OpenBottomSheet -> openBottomSheet(screen)
            is NavigationIntent.CloseBottomSheet -> closeBottomSheet()
        }
    }
}