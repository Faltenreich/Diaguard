package com.faltenreich.diaguard

import app.cash.turbine.test
import com.faltenreich.diaguard.startup.seed.ImportSeedUseCase
import com.faltenreich.diaguard.test.TestSuite
import kotlinx.coroutines.test.runTest
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertTrue

class AppViewModelTest : TestSuite(appModule()) {

    private val viewModel: AppViewModel by inject()
    private val importSeed: ImportSeedUseCase by inject()

    @Test
    fun `indicates first start initially`() = runTest {
        viewModel.state.test {
            assertTrue(awaitItem() is AppState.FirstStart)
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `indicates subsequent start if seed has been imported`() = runTest {
        importSeed()

        viewModel.state.test {
            assertTrue(awaitItem() is AppState.SubsequentStart)
            ensureAllEventsConsumed()
        }
    }
}