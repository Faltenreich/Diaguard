package com.faltenreich.diaguard.measurement.category.list

import app.cash.turbine.test
import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.measurement.category.form.MeasurementCategoryFormModal
import com.faltenreich.diaguard.measurement.category.form.MeasurementCategoryFormScreen
import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.navigation.NavigationEvent
import com.faltenreich.diaguard.shared.database.DatabaseKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.koin.test.inject
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MeasurementCategoryListViewModelTest : TestSuite {

    private val viewModel: MeasurementCategoryListViewModel by inject()
    private val navigation: Navigation by inject()

    @BeforeTest
    override fun beforeTest() {
        super.beforeTest()
        importSeed()
    }

    @Test
    fun `launch with categories`() = runTest {
        viewModel.state.test {
            assertTrue(awaitItem().categories.isNotEmpty())
        }
    }

    @Test
    fun `launch with category for every database key`() = runTest {
        viewModel.state.test {
            val categories = awaitItem().categories
            DatabaseKey.MeasurementCategory.entries.forEach { key ->
                assertTrue(categories.any { it.key == key })
            }
        }
    }

    @Test
    fun `decrement sort index of category`() = runTest {
        viewModel.state.test {
            val category = awaitItem().categories.last()
            viewModel.handleIntent(MeasurementCategoryListIntent.DecrementSortIndex(category))

            val update = awaitItem().categories.first { it.id == category.id }
            assertEquals(
                expected = category.sortIndex - 1,
                actual = update.sortIndex,
            )
            awaitItem()
        }
    }

    @Test
    fun `increment sort index of category`() = runTest {
        viewModel.state.test {
            val category = awaitItem().categories.first()
            viewModel.handleIntent(MeasurementCategoryListIntent.IncrementSortIndex(category))

            val update = awaitItem().categories.first { it.id == category.id }
            assertEquals(
                expected = category.sortIndex + 1,
                actual = update.sortIndex,
            )
            awaitItem()
        }
    }

    @Test
    fun `push screen when editing category`() = runTest {
        navigation.events.test {
            val category = viewModel.state.first().categories.first()
            viewModel.handleIntent(MeasurementCategoryListIntent.Edit(category))

            val event = awaitItem()
            assertTrue(event is NavigationEvent.PushScreen)
            assertTrue(event.screen is MeasurementCategoryFormScreen)
        }
    }

    @Test
    fun `push screen when creating category`() = runTest {
        navigation.events.test {
            viewModel.handleIntent(MeasurementCategoryListIntent.Create)

            val event = awaitItem()
            assertTrue(event is NavigationEvent.OpenModal)
            assertTrue(event.modal is MeasurementCategoryFormModal)
        }
    }
}