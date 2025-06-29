package com.faltenreich.diaguard

import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertTrue

class AppViewModelTest : TestSuite {

    private val viewModel: AppViewModel by inject()

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