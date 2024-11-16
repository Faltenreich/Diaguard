package com.faltenreich.diaguard.measurement.category.form

import app.cash.turbine.test
import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryRepository
import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.navigation.NavigationEvent
import com.faltenreich.diaguard.shared.view.AlertModal
import com.faltenreich.diaguard.shared.view.DeleteModal
import com.faltenreich.diaguard.shared.view.EmojiModal
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.koin.core.parameter.parametersOf
import org.koin.test.get
import org.koin.test.inject
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MeasurementCategoryFormViewModelTest : TestSuite {

    private val navigation: Navigation by inject()
    private val categoryRepository: MeasurementCategoryRepository by inject()

    private lateinit var viewModel: MeasurementCategoryFormViewModel

    @BeforeTest
    override fun beforeTest() {
        super.beforeTest()
        importSeed()
    }

    @Test
    fun `launch with category`() = runTest {
        val category = categoryRepository.observeAll().first().first()
        viewModel = get(parameters = { parametersOf(category.id) })

        assertEquals(expected = category.icon, actual = viewModel.icon.value)
        assertEquals(expected = category.name, actual = viewModel.name.value)
        assertEquals(expected = category.isActive, actual = viewModel.isActive.value)

        viewModel.state.test {
            assertTrue(awaitItem().properties.isNotEmpty())
        }
    }

    @Test
    fun `open modal when intending to open icon picker`() = runTest {
        val category = categoryRepository.observeAll().first().first()
        viewModel = get(parameters = { parametersOf(category.id) })

        navigation.events.test {
            viewModel.handleIntent(MeasurementCategoryFormIntent.OpenIconPicker)

            val event = awaitItem()
            assertTrue(event is NavigationEvent.OpenModal)
            assertTrue(event.modal is EmojiModal)
        }
    }

    @Test
    fun `store category and pop screen when updating category`() = runTest {
        val category = categoryRepository.observeAll().first().first()
        val update = "test"
        viewModel = get(parameters = { parametersOf(category.id) })
        viewModel.name.value = update

        navigation.events.test {
            viewModel.handleIntent(MeasurementCategoryFormIntent.UpdateCategory)

            assertEquals(
                expected = update,
                actual = categoryRepository.getById(category.id)!!.name,
            )
            assertTrue(awaitItem() is NavigationEvent.PopScreen)
        }
    }

    @Test
    fun `open confirmation modal when intending to delete user-generated category`() = runTest {
        val categoryId = categoryRepository.create(
            MeasurementCategory.User(
                name = "name",
                icon = "icon",
                sortIndex = 999,
                isActive = true,
            )
        )
        viewModel = get(parameters = { parametersOf(categoryId) })

        navigation.events.test {
            viewModel.handleIntent(MeasurementCategoryFormIntent.DeleteCategory)

            val event = awaitItem()
            assertTrue(event is NavigationEvent.OpenModal)
            assertTrue(event.modal is DeleteModal)
        }
    }

    @Test
    fun `open alert modal when intending to delete seed category`() = runTest {
        val category = categoryRepository.observeAll().first().first()
        viewModel = get(parameters = { parametersOf(category.id) })

        navigation.events.test {
            viewModel.handleIntent(MeasurementCategoryFormIntent.DeleteCategory)

            val event = awaitItem()
            assertTrue(event is NavigationEvent.OpenModal)
            assertTrue(event.modal is AlertModal)
        }
    }
}