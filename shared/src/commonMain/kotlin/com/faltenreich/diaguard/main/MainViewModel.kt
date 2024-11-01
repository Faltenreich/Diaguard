package com.faltenreich.diaguard.main

import com.faltenreich.diaguard.dashboard.DashboardScreen
import com.faltenreich.diaguard.log.LogScreen
import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.navigation.NavigationEvent
import com.faltenreich.diaguard.navigation.bottomsheet.CloseBottomSheetUseCase
import com.faltenreich.diaguard.navigation.bottomsheet.GetBottomSheetUseCase
import com.faltenreich.diaguard.navigation.bottomsheet.OpenBottomSheetUseCase
import com.faltenreich.diaguard.navigation.modal.GetModalUseCase
import com.faltenreich.diaguard.navigation.screen.GetBottomAppBarStyleUseCase
import com.faltenreich.diaguard.navigation.screen.GetTopAppBarStyleUseCase
import com.faltenreich.diaguard.navigation.screen.NavigateBackUseCase
import com.faltenreich.diaguard.preference.StartScreen
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.architecture.combine
import com.faltenreich.diaguard.timeline.TimelineScreen
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class MainViewModel(
    getPreference: GetPreferenceUseCase,
    getTopAppBarStyle: GetTopAppBarStyleUseCase,
    getBottomAppBarStyle: GetBottomAppBarStyleUseCase,
    getBottomSheet: GetBottomSheetUseCase,
    getModal: GetModalUseCase,
    hasData: HasDataUseCase,
    private val setup: SetupUseCase,
    private val navigation: Navigation,
    private val popScreen: NavigateBackUseCase,
    private val openBottomSheet: OpenBottomSheetUseCase,
    private val closeBottomSheet: CloseBottomSheetUseCase,
) : ViewModel<MainState, MainIntent, Unit>() {

    override val state = combine(
        hasData(),
        getPreference(StartScreen.Preference),
        getTopAppBarStyle(),
        getBottomAppBarStyle(),
        getBottomSheet(),
        getModal(),
    ) { hasData, startScreen, topAppBarStyle, bottomAppBarStyle, bottomSheet, modal ->
        if (hasData) {
            MainState.SubsequentStart(
                startScreen = when (startScreen) {
                    StartScreen.DASHBOARD -> DashboardScreen
                    StartScreen.TIMELINE -> TimelineScreen
                    StartScreen.LOG -> LogScreen
                },
                topAppBarStyle = topAppBarStyle,
                bottomAppBarStyle = bottomAppBarStyle,
                bottomSheet = bottomSheet,
                modal = modal,
            )
        } else {
            MainState.FirstStart
        }
    }.distinctUntilChanged()

    init {
        scope.launch { setup() }
    }

    override suspend fun handleIntent(intent: MainIntent) = with(intent) {
        when (this) {
            is MainIntent.PopScreen -> popScreen()
            is MainIntent.OpenBottomSheet -> openBottomSheet(screen)
            is MainIntent.CloseBottomSheet -> closeBottomSheet()
        }
    }

    suspend fun collectNavigationEvents(onEvent: (NavigationEvent) -> Unit) {
        navigation.collectEvents(onEvent)
    }
}