package com.faltenreich.diaguard.preference.overview

import app.cash.turbine.test
import com.faltenreich.diaguard.TestSuite
import kotlinx.coroutines.test.runTest
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertEquals

class OverviewPreferenceListViewModelTest : TestSuite {

    private val viewModel: OverviewPreferenceListViewModel by inject()

    @Test
    fun `launch with 1 decimal places and both buttons enabled`() = runTest {
        viewModel.state.test {
            assertEquals(
                expected = OverviewPreferenceListState.DecimalPlaces(
                    selection = 1,
                    illustration = "decimal_places_illustration",
                    enableDecreaseButton = true,
                    enableIncreaseButton = true,
                ),
                actual = awaitItem().decimalPlaces,
            )
        }
    }

    @Test
    fun `disable decrease button when updating decimal places to 0`() = runTest {
        viewModel.handleIntent(OverviewPreferenceListIntent.SetDecimalPlaces(decimalPlaces = 0))

        viewModel.state.test {
            assertEquals(
                expected = OverviewPreferenceListState.DecimalPlaces(
                    selection = 0,
                    illustration = "decimal_places_illustration",
                    enableDecreaseButton = false,
                    enableIncreaseButton = true,
                ),
                actual = awaitItem().decimalPlaces,
            )
        }
    }

    @Test
    fun `disable increase button when updating decimal places to 3`() = runTest {
        viewModel.handleIntent(OverviewPreferenceListIntent.SetDecimalPlaces(decimalPlaces = 3))

        viewModel.state.test {
            assertEquals(
                expected = OverviewPreferenceListState.DecimalPlaces(
                    selection = 3,
                    illustration = "decimal_places_illustration",
                    enableDecreaseButton = true,
                    enableIncreaseButton = false,
                ),
                actual = awaitItem().decimalPlaces,
            )
        }
    }

    @Test
    fun `do nothing when updating decimal places to 4`() = runTest {
        viewModel.handleIntent(OverviewPreferenceListIntent.SetDecimalPlaces(decimalPlaces = 4))

        viewModel.state.test {
            assertEquals(
                expected = OverviewPreferenceListState.DecimalPlaces(
                    selection = 1,
                    illustration = "decimal_places_illustration",
                    enableDecreaseButton = true,
                    enableIncreaseButton = true,
                ),
                actual = awaitItem().decimalPlaces,
            )
        }
    }
}