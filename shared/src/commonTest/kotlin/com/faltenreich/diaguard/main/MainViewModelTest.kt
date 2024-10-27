package com.faltenreich.diaguard.main

import app.cash.turbine.test
import com.faltenreich.diaguard.TestSuite
import kotlinx.coroutines.test.runTest
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertTrue

class MainViewModelTest : TestSuite {

    private val viewModel: MainViewModel by inject()

    @Test
    fun `is loading on start`() = runTest {
        viewModel.state.test {
            assertTrue(awaitItem() is MainState.FirstStart)
            assertTrue(awaitItem() is MainState.SubsequentStart)
        }
    }
}