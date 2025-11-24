package com.faltenreich.diaguard.main

import app.cash.turbine.test
import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.dashboard.DashboardScreen
import com.faltenreich.diaguard.log.LogScreen
import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.navigation.NavigationEvent
import com.faltenreich.diaguard.preference.screen.StartScreen
import com.faltenreich.diaguard.preference.screen.StartScreenPreference
import com.faltenreich.diaguard.preference.store.SetPreferenceUseCase
import com.faltenreich.diaguard.preference.version.VersionCodePreference
import com.faltenreich.diaguard.startup.seed.ImportSeedUseCase
import com.faltenreich.diaguard.timeline.TimelineScreen
import kotlinx.coroutines.test.runTest
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MainViewModelTest : TestSuite() {

    private val importSeed: ImportSeedUseCase by inject()
    private val setPreference: SetPreferenceUseCase by inject()
    private val navigation: Navigation by inject()
    private val viewModel: MainViewModel by inject()

    @Test
    fun `shows dashboard as start screen by default`() = runTest {
        setPreference(VersionCodePreference, 1)
        importSeed()

        viewModel.state.test {
            val state = awaitItem()
            assertEquals(DashboardScreen, state.startScreen)
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `shows timeline as start screen if selected`() = runTest {
        setPreference(VersionCodePreference, 1)
        importSeed()
        setPreference(StartScreenPreference, StartScreen.TIMELINE)

        viewModel.state.test {
            val state = awaitItem()
            assertEquals(TimelineScreen, state.startScreen)
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `shows log as start screen if selected`() = runTest {
        setPreference(VersionCodePreference, 1)
        importSeed()
        setPreference(StartScreenPreference, StartScreen.LOG)

        viewModel.state.test {
            val state = awaitItem()
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
}