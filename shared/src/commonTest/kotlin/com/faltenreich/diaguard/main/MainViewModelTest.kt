package com.faltenreich.diaguard.main

import app.cash.turbine.test
import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.dashboard.DashboardScreen
import com.faltenreich.diaguard.log.LogScreen
import com.faltenreich.diaguard.main.menu.MainMenuScreen
import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.navigation.NavigationEvent
import com.faltenreich.diaguard.preference.screen.StartScreen
import com.faltenreich.diaguard.preference.store.SetPreferenceUseCase
import com.faltenreich.diaguard.timeline.TimelineScreen
import kotlinx.coroutines.test.runTest
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MainViewModelTest : TestSuite {

    private val viewModel: MainViewModel by inject()
    private val setPreference: SetPreferenceUseCase by inject()
    private val navigation: Navigation by inject()

    @Test
    fun `shows loading on first start`() = runTest {
        viewModel.state.test {
            assertTrue(awaitItem() is MainState.FirstStart)
            assertTrue(awaitItem() is MainState.SubsequentStart)
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `shows content if seed has been imported`() = runTest {
        importSeed()

        viewModel.state.test {
            assertTrue(awaitItem() is MainState.SubsequentStart)
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `shows dashboard as start screen by default`() = runTest {
        importSeed()

        viewModel.state.test {
            val state = awaitItem()
            assertTrue(state is MainState.SubsequentStart)
            assertEquals(DashboardScreen, state.startScreen)
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `shows timeline as start screen if selected`() = runTest {
        importSeed()
        setPreference(StartScreen.Preference, StartScreen.TIMELINE)

        viewModel.state.test {
            val state = awaitItem()
            assertTrue(state is MainState.SubsequentStart)
            assertEquals(TimelineScreen, state.startScreen)
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `shows log as start screen if selected`() = runTest {
        importSeed()
        setPreference(StartScreen.Preference, StartScreen.LOG)

        viewModel.state.test {
            val state = awaitItem()
            assertTrue(state is MainState.SubsequentStart)
            assertEquals(LogScreen, state.startScreen)
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `closes screen`() = runTest {
        navigation.events.test {
            viewModel.handleIntent(MainIntent.PopScreen)
            assertTrue(awaitItem() is NavigationEvent.PopScreen)
        }
    }

    @Test
    fun `opens bottom sheet`() = runTest {
        navigation.events.test {
            val screen = MainMenuScreen(currentDestination = "")
            viewModel.handleIntent(MainIntent.OpenBottomSheet(screen))

            val event = awaitItem()
            assertTrue(event is NavigationEvent.OpenBottomSheet)
            assertEquals(
                expected = screen,
                actual = event.bottomSheet,
            )
        }
    }

    @Test
    fun `closes bottom sheet`() = runTest {
        navigation.events.test {
            val screen = MainMenuScreen(currentDestination = "")
            viewModel.handleIntent(MainIntent.OpenBottomSheet(screen))
            viewModel.handleIntent(MainIntent.CloseBottomSheet)

            assertTrue(awaitItem() is NavigationEvent.OpenBottomSheet)
            assertTrue(awaitItem() is NavigationEvent.CloseBottomSheet)
        }
    }
}