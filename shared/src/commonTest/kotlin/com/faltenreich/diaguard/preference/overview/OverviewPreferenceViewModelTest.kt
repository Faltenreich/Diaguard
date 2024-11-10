package com.faltenreich.diaguard.preference.overview

import app.cash.turbine.test
import com.faltenreich.diaguard.TestSuite
import kotlinx.coroutines.test.runTest
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertEquals

class OverviewPreferenceViewModelTest : TestSuite {

    private val viewModel: OverviewPreferenceViewModel by inject()

    @Test
    fun `show preferences`() = runTest {
        viewModel.state.test {
            assertEquals(
                expected = listOf(),
                actual = awaitItem(),
            )
        }
    }
}