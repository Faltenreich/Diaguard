package com.faltenreich.diaguard.main

import com.faltenreich.diaguard.backup.ImportUseCase
import com.faltenreich.diaguard.dashboard.DashboardScreen
import com.faltenreich.diaguard.log.LogScreen
import com.faltenreich.diaguard.navigation.bottomsheet.CloseBottomSheetUseCase
import com.faltenreich.diaguard.navigation.bottomsheet.GetBottomSheetUseCase
import com.faltenreich.diaguard.navigation.bottomsheet.OpenBottomSheetUseCase
import com.faltenreich.diaguard.navigation.modal.GetModalUseCase
import com.faltenreich.diaguard.navigation.screen.GetBottomAppBarStyleUseCase
import com.faltenreich.diaguard.navigation.screen.GetCurrentScreenUseCase
import com.faltenreich.diaguard.navigation.screen.GetTopAppBarStyleUseCase
import com.faltenreich.diaguard.preference.StartScreen
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.architecture.combine
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.timeline.TimelineScreen
import kotlinx.coroutines.launch

class MainViewModel(
    getPreference: GetPreferenceUseCase = inject(),
    getCurrentScreen: GetCurrentScreenUseCase = inject(),
    getTopAppBarStyle: GetTopAppBarStyleUseCase = inject(),
    getBottomAppBarStyle: GetBottomAppBarStyleUseCase = inject(),
    getBottomSheet: GetBottomSheetUseCase = inject(),
    getModal: GetModalUseCase = inject(),
    private val hasData: HasDataUseCase = inject(),
    private val importData: ImportUseCase = inject(),
    private val openBottomSheet: OpenBottomSheetUseCase = inject(),
    private val closeBottomSheet: CloseBottomSheetUseCase = inject(),
) : ViewModel<MainState, MainIntent, Unit>() {

    override val state = combine(
        hasData(),
        getPreference(StartScreen.Preference),
        getCurrentScreen(),
        getTopAppBarStyle(),
        getBottomAppBarStyle(),
        getBottomSheet(),
        getModal(),
    ) { hasData, startScreen, currentScreen, topAppBarStyle, bottomAppBarStyle, bottomSheet, modal ->
        if (hasData) {
            MainState.SubsequentStart(
                startScreen = when (startScreen) {
                    StartScreen.DASHBOARD -> DashboardScreen
                    StartScreen.TIMELINE -> TimelineScreen
                    StartScreen.LOG -> LogScreen
                },
                currentScreen = currentScreen,
                topAppBarStyle = topAppBarStyle,
                bottomAppBarStyle = bottomAppBarStyle,
                bottomSheet = bottomSheet,
                modal = modal,
            )
        } else {
            MainState.FirstStart
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

    override suspend fun handleIntent(intent: MainIntent) = with(intent) {
        when (this) {
            is MainIntent.OpenBottomSheet -> openBottomSheet(screen)
            is MainIntent.CloseBottomSheet -> closeBottomSheet()
        }
    }
}