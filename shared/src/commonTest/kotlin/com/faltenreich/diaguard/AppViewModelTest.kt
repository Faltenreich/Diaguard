package com.faltenreich.diaguard

import app.cash.turbine.test
import com.faltenreich.diaguard.preference.store.SetPreferenceUseCase
import com.faltenreich.diaguard.preference.version.VersionCodePreference
import kotlinx.coroutines.test.runTest
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertTrue

class AppViewModelTest : TestSuite {

    private val viewModel: AppViewModel by inject()
    private val setPreference: SetPreferenceUseCase by inject()

    @Test
    fun `shows loading on first start`() = runTest {
        viewModel.state.test {
            assertTrue(awaitItem() is AppState.FirstStart)
            assertTrue(awaitItem() is AppState.SubsequentStart)
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `shows content if seed has been imported`() = runTest {
        setPreference(VersionCodePreference, 1)
        importSeed()

        viewModel.state.test {
            assertTrue(awaitItem() is AppState.SubsequentStart)
            ensureAllEventsConsumed()
        }
    }
}