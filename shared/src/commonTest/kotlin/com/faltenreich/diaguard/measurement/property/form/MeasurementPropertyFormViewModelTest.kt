package com.faltenreich.diaguard.measurement.property.form

import app.cash.turbine.test
import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryRepository
import com.faltenreich.diaguard.measurement.property.MeasurementAggregationStyle
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitRepository
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.navigation.NavigationEvent
import com.faltenreich.diaguard.shared.view.AlertModal
import com.faltenreich.diaguard.shared.view.DeleteModal
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.koin.core.parameter.parametersOf
import org.koin.test.get
import org.koin.test.inject
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MeasurementPropertyFormViewModelTest : TestSuite {

    private val navigation: Navigation by inject()
    private val categoryRepository: MeasurementCategoryRepository by inject()
    private val propertyRepository: MeasurementPropertyRepository by inject()
    private val unitRepository: MeasurementUnitRepository by inject()

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
        assertEquals(expected = property.selectedUnit, actual = viewModel.selectedUnit.value)
        assertEquals(expected = property.aggregationStyle, actual = viewModel.aggregationStyle.value)
        assertEquals(expected = property.range.minimum.toString(), actual = viewModel.valueRangeMinimum.value)
        assertEquals(expected = property.range.low?.toString(), actual = viewModel.valueRangeLow.value)
        assertEquals(expected = property.range.target?.toString(), actual = viewModel.valueRangeTarget.value)
        assertEquals(expected = property.range.high?.toString(), actual = viewModel.valueRangeHigh.value)
        assertEquals(expected = property.range.maximum.toString(), actual = viewModel.valueRangeMaximum.value)
        assertEquals(expected = property.range.isHighlighted, actual = viewModel.isValueRangeHighlighted.value)
        assertEquals(expected = property.selectedUnit.abbreviation, actual = viewModel.unitName.value)
    }

    @Test
    fun `store category and pop screen when updating category`() = runTest {
        val property = propertyRepository.getAll().first()
        val update = "test"
        viewModel = get(parameters = { parametersOf(property.id) })
        viewModel.propertyName.value = update

        navigation.events.test {
            viewModel.handleIntent(MeasurementPropertyFormIntent.UpdateProperty)

            assertEquals(
                expected = update,
                actual = propertyRepository.getById(property.id)!!.name,
            )
            assertTrue(awaitItem() is NavigationEvent.PopScreen)
        }
    }

    @Test
    fun `open confirmation modal when intending to delete user-generated category`() = runTest {
        val category = categoryRepository.observeAll().first().first()
        val propertyId = propertyRepository.create(
            MeasurementProperty.User(
                name = "name",
                sortIndex = 999,
                aggregationStyle = MeasurementAggregationStyle.CUMULATIVE,
                range = MeasurementValueRange(
                    minimum = 0.0,
                    low = 0.0,
                    target = 0.0,
                    high = 0.0,
                    maximum = 0.0,
                    isHighlighted = true,
                ),
                category = category,
            )
        )
        unitRepository.create(
            MeasurementUnit.User(
                name = "name",
                abbreviation = "abbreviation",
                factor = 1.0,
                isSelected = true,
                propertyId = propertyId,
            )
        )
        viewModel = get(parameters = { parametersOf(propertyId) })

        navigation.events.test {
            viewModel.handleIntent(MeasurementPropertyFormIntent.DeleteProperty)

            val event = awaitItem()
            assertTrue(event is NavigationEvent.OpenModal)
            assertTrue(event.modal is DeleteModal)
        }
    }

    @Test
    fun `open alert modal when intending to delete seed category`() = runTest {
        val property = propertyRepository.getAll().first()
        viewModel = get(parameters = { parametersOf(property.id) })

        navigation.events.test {
            viewModel.handleIntent(MeasurementPropertyFormIntent.DeleteProperty)

            val event = awaitItem()
            assertTrue(event is NavigationEvent.OpenModal)
            assertTrue(event.modal is AlertModal)
        }
    }
}