package com.faltenreich.diaguard.measurement.property.form

import app.cash.turbine.test
import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.navigation.NavigationEvent
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

    private val navigation: Navigation by inject()
    private val propertyRepository: MeasurementPropertyRepository by inject()

    private lateinit var viewModel: MeasurementPropertyFormViewModel

    @BeforeTest
    override fun beforeTest() {
        super.beforeTest()
        importSeed()
    }

    @Test
    fun `launch with category`() = runTest {
        val property = propertyRepository.getAll().first()
        viewModel = get(parameters = { parametersOf(property.id) })

        assertEquals(expected = property.name, actual = viewModel.propertyName.value)
        assertEquals(expected = property.aggregationStyle, actual = viewModel.aggregationStyle.value)
        assertEquals(expected = property.range.minimum.toString(), actual = viewModel.valueRangeMinimum.value)
        assertEquals(expected = property.range.low?.toString(), actual = viewModel.valueRangeLow.value)
        assertEquals(expected = property.range.target?.toString(), actual = viewModel.valueRangeTarget.value)
        assertEquals(expected = property.range.high?.toString(), actual = viewModel.valueRangeHigh.value)
        assertEquals(expected = property.range.maximum.toString(), actual = viewModel.valueRangeMaximum.value)
        assertEquals(expected = property.range.isHighlighted, actual = viewModel.isValueRangeHighlighted.value)

        viewModel.state.test {
            assertEquals(property.unit, awaitItem().unit)
        }
    }

    @Test
    fun `store category and pop screen when updating category`() = runTest {
        val property = propertyRepository.getAll().first()
        val update = "test"
        viewModel = get(parameters = { parametersOf(property.id) })
        viewModel.propertyName.value = update

        navigation.events.test {
            viewModel.handleIntent(MeasurementPropertyFormIntent.Submit)

            assertEquals(
                expected = update,
                actual = propertyRepository.getById(property.id)!!.name,
            )
            assertTrue(awaitItem() is NavigationEvent.PopScreen)
        }
    }

    @Ignore
    @Test
    fun `open confirmation modal when intending to delete user-generated category`() = runTest {
        val property = propertyRepository.getAll().first()
        propertyRepository.update(property.copy(key = null))

        // FIXME: Race condition, property has still a key
        viewModel = get(parameters = { parametersOf(property.id) })

        viewModel.state.test {
            viewModel.handleIntent(MeasurementPropertyFormIntent.Delete(needsConfirmation = true))
            assertNotNull(awaitItem().alertDialog)
        }
    }

    @Test
    fun `open alert modal when intending to delete seed category`() = runTest {
        val property = propertyRepository.getAll().first()
        viewModel = get(parameters = { parametersOf(property.id) })
        viewModel.handleIntent(MeasurementPropertyFormIntent.Delete(needsConfirmation = true))

        viewModel.state.test {
            assertNotNull(awaitItem().alertDialog)
        }
    }
}