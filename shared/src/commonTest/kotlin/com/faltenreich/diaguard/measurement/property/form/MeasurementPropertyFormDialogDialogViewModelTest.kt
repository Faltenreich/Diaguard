package com.faltenreich.diaguard.measurement.property.form

import app.cash.turbine.test
import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.data.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.navigation.NavigationEvent
import com.faltenreich.diaguard.startup.seed.ImportSeedUseCase
import kotlinx.coroutines.test.runTest
import org.koin.core.parameter.parametersOf
import org.koin.test.get
import org.koin.test.inject
import kotlin.test.BeforeTest
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class MeasurementPropertyFormDialogDialogViewModelTest : TestSuite {

    private val importSeed: ImportSeedUseCase by inject()
    private val navigation: Navigation by inject()
    private val propertyRepository: MeasurementPropertyRepository by inject()

    private lateinit var viewModel: MeasurementPropertyFormViewModel

    @BeforeTest
    override fun beforeTest() {
        super.beforeTest()
        importSeed()
    }

    @Test
    fun `launch with property`() = runTest {
        val property = propertyRepository.getAll().first()
        viewModel = get(parameters = { parametersOf(property.category.id, property.id) })

        viewModel.state.test {
            val state = awaitItem()

            assertEquals(expected = property.name, actual = state.property.name)
            assertEquals(expected = property.aggregationStyle, actual = state.property.aggregationStyle)
            assertEquals(expected = property.range.isHighlighted, actual = state.valueRange.isHighlighted)

            assertEquals(property.unit, state.property.unit)
        }
    }

    @Test
    fun `store property and pop screen on submit`() = runTest {
        val property = propertyRepository.getAll().first()
        viewModel = get(parameters = { parametersOf(property.category.id, property.id) })

        navigation.events.test {
            val name = "test"
            val valueRange = MeasurementPropertyFormState.ValueRange(
                minimum = "0.0",
                low = "1.0",
                target = "2.0",
                high = "3.0",
                maximum = "4.0",
                isHighlighted = true,
                unit = "",
            )

            viewModel.handleIntent(MeasurementPropertyFormIntent.UpdateProperty(name = name))
            viewModel.handleIntent(MeasurementPropertyFormIntent.UpdateValueRange(valueRange = valueRange))
            viewModel.handleIntent(MeasurementPropertyFormIntent.Submit)

            val update = propertyRepository.getById(property.id)
            assertNotNull(update)

            assertEquals(expected = name, actual = update.name)
            assertEquals(expected = valueRange.minimum.toDouble(), actual = update.range.minimum)
            assertEquals(expected = valueRange.low.toDouble(), actual = update.range.low)
            assertEquals(expected = valueRange.target.toDouble(), actual = update.range.target)
            assertEquals(expected = valueRange.high.toDouble(), actual = update.range.high)
            assertEquals(expected = valueRange.maximum.toDouble(), actual = update.range.maximum)
            assertEquals(expected = valueRange.isHighlighted, actual = update.range.isHighlighted)

            assertTrue(awaitItem() is NavigationEvent.PopScreen)
        }
    }

    @Ignore
    @Test
    fun `open confirmation modal when intending to delete user-generated property`() = runTest {
        val property = propertyRepository.getAll().first()
        propertyRepository.update(property.copy(key = null))

        // FIXME: Race condition, property has still a key
        viewModel = get(parameters = { parametersOf(property.category.id, property.id) })

        viewModel.state.test {
            viewModel.handleIntent(MeasurementPropertyFormIntent.Delete(needsConfirmation = true))
            assertTrue(awaitItem().dialog is MeasurementPropertyFormState.Dialog.Alert)
        }
    }

    @Test
    fun `open alert modal when intending to delete seed property`() = runTest {
        val property = propertyRepository.getAll().first()
        viewModel = get(parameters = { parametersOf(property.category.id, property.id) })
        viewModel.handleIntent(MeasurementPropertyFormIntent.Delete(needsConfirmation = true))

        viewModel.state.test {
            assertTrue(awaitItem().dialog is MeasurementPropertyFormState.Dialog.Alert)
        }
    }
}