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
            awaitComplete()
        }
    }

    @Test
    fun `disable decrease button when reaching 0 decimal places`() = runTest {
        viewModel.state.test {
            viewModel.handleIntent(DecimalPlacesFormIntent.Update(decimalPlaces = 0))

            // FIXME: FakeKeyValueStore does not publish update
            assertEquals(
                expected = DecimalPlacesFormState(
                    decimalPlaces = 0,
                    illustration = "decimal_places_illustration",
                    enableDecreaseButton = false,
                    enableIncreaseButton = true,
                ),
                actual = awaitItem(),
            )
            awaitComplete()
        }
    }
}