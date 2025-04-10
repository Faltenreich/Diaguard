package com.faltenreich.diaguard.preference.decimal

import app.cash.turbine.test
import com.faltenreich.diaguard.TestSuite
import kotlinx.coroutines.test.runTest
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertEquals

class DecimalPlacesFormViewModelTest : TestSuite {

    private val viewModel: DecimalPlacesFormViewModel by inject()

    @Test
    fun `launch with 1 decimal places and both buttons enabled`() = runTest {
        viewModel.state.test {
            assertEquals(
                expected = DecimalPlacesFormState(
                    decimalPlaces = 1,
                    illustration = "decimal_places_illustration",
                    enableDecreaseButton = true,
                    enableIncreaseButton = true,
                ),
                actual = awaitItem(),
            )
        }
    }

    @Test
    fun `disable decrease button when updating decimal places to 0`() = runTest {
        viewModel.handleIntent(DecimalPlacesFormIntent.Update(decimalPlaces = 0))

        viewModel.state.test {
            assertEquals(
                expected = DecimalPlacesFormState(
                    decimalPlaces = 0,
                    illustration = "decimal_places_illustration",
                    enableDecreaseButton = false,
                    enableIncreaseButton = true,
                ),
                actual = awaitItem(),
            )
        }
    }

    @Test
    fun `disable increase button when updating decimal places to 3`() = runTest {
        viewModel.handleIntent(DecimalPlacesFormIntent.Update(decimalPlaces = 3))

        viewModel.state.test {
            assertEquals(
                expected = DecimalPlacesFormState(
                    decimalPlaces = 3,
                    illustration = "decimal_places_illustration",
                    enableDecreaseButton = true,
                    enableIncreaseButton = false,
                ),
                actual = awaitItem(),
            )
        }
    }

    @Test
    fun `do nothing when updating decimal places to 4`() = runTest {
        viewModel.handleIntent(DecimalPlacesFormIntent.Update(decimalPlaces = 4))

        viewModel.state.test {
            assertEquals(
                expected = DecimalPlacesFormState(
                    decimalPlaces = 1,
                    illustration = "decimal_places_illustration",
                    enableDecreaseButton = true,
                    enableIncreaseButton = true,
                ),
                actual = awaitItem(),
            )
        }
    }
}