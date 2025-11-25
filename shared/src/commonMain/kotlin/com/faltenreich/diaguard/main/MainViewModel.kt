package com.faltenreich.diaguard.main

import com.faltenreich.diaguard.architecture.viewmodel.ViewModel
import com.faltenreich.diaguard.dashboard.DashboardScreen
import com.faltenreich.diaguard.log.LogScreen
import com.faltenreich.diaguard.navigation.bar.bottom.GetBottomAppBarStyleUseCase
import com.faltenreich.diaguard.navigation.bar.top.GetTopAppBarStyleUseCase
import com.faltenreich.diaguard.navigation.screen.CollectNavigationEventsUseCase
import com.faltenreich.diaguard.navigation.screen.NavigateBackUseCase
import com.faltenreich.diaguard.navigation.screen.NavigateToUseCase
import com.faltenreich.diaguard.preference.color.ColorSchemePreference
import com.faltenreich.diaguard.preference.screen.StartScreen
import com.faltenreich.diaguard.preference.screen.StartScreenPreference
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.timeline.TimelineScreen
import com.faltenreich.diaguard.view.window.WindowController
import kotlinx.coroutines.flow.combine

class MainViewModel(
    getPreference: GetPreferenceUseCase,
    getTopAppBarStyle: GetTopAppBarStyleUseCase,
    getBottomAppBarStyle: GetBottomAppBarStyleUseCase,
    private val windowController: WindowController,
    private val navigateTo: NavigateToUseCase,
    private val navigateBack: NavigateBackUseCase,
    val collectNavigationEvents: CollectNavigationEventsUseCase,
) : ViewModel<MainState, MainIntent, Unit>() {

    override val state = combine(
        getPreference(StartScreenPreference),
        getPreference(ColorSchemePreference),
        getTopAppBarStyle(),
        getBottomAppBarStyle(),
    ) { startScreen, colorScheme, topAppBarStyle, bottomAppBarStyle ->
        MainState(
            startScreen = when (startScreen) {
                StartScreen.DASHBOARD -> DashboardScreen
                StartScreen.TIMELINE -> TimelineScreen
                StartScreen.LOG -> LogScreen
            },
            colorScheme = colorScheme,
            topAppBarStyle = topAppBarStyle,
            bottomAppBarStyle = bottomAppBarStyle,
        )
    }

    override suspend fun handleIntent(intent: MainIntent) = with(intent) {
        when (this) {
            is MainIntent.TintStatusBars ->
                windowController.setIsAppearanceLightStatusBars(isAppearanceLightStatusBars)
            is MainIntent.NavigateTo -> navigateTo(target, popHistory)
            is MainIntent.NavigateBack -> navigateBack()
        }
    }
}