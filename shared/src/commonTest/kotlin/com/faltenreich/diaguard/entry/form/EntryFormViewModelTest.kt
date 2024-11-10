package com.faltenreich.diaguard.entry.form

import app.cash.turbine.test
import app.cash.turbine.turbineScope
import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.datetime.picker.DatePickerModal
import com.faltenreich.diaguard.datetime.picker.TimePickerModal
import com.faltenreich.diaguard.entry.tag.EntryTagRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.navigation.NavigationEvent
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.shared.view.DeleteModal
import com.faltenreich.diaguard.tag.Tag
import kotlinx.coroutines.test.runTest
import org.koin.core.parameter.parametersOf
import org.koin.test.get
import org.koin.test.inject
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class EntryFormViewModelTest : TestSuite {

    private val navigation: Navigation by inject()
    private val entryRepository: EntryTagRepository by inject()
    private val valueRepository: MeasurementValueRepository by inject()

    private lateinit var viewModel: EntryFormViewModel

    @BeforeTest
    override fun beforeTest() {
        super.beforeTest()
        importSeed()
    }

    @Test
    fun `launch with no and then some categories`() = runTest {
        viewModel = get(parameters = { parametersOf(null, null, null) })

        viewModel.state.test {
            assertTrue(awaitItem().measurements.isEmpty())
            assertTrue(awaitItem().measurements.isNotEmpty())
        }
    }

    @Test
    fun `launch with tags`() = runTest {
        viewModel = get(parameters = { parametersOf(null, null, null) })

        viewModel.state.test {
            assertTrue(awaitItem().tags.isNotEmpty())
        }
    }

    @Test
    fun `open dialog when selecting date`() = runTest {
        viewModel = get(parameters = { parametersOf(null, null, null) })

        navigation.events.test {
            viewModel.handleIntent(EntryFormIntent.SelectDate)

            val event = awaitItem()
            assertTrue(event is NavigationEvent.OpenModal)
            assertTrue(event.modal is DatePickerModal)
        }
    }

    @Test
    fun `open dialog when selecting time`() = runTest {
        viewModel = get(parameters = { parametersOf(null, null, null) })

        navigation.events.test {
            viewModel.handleIntent(EntryFormIntent.SelectTime)

            val event = awaitItem()
            assertTrue(event is NavigationEvent.OpenModal)
            assertTrue(event.modal is TimePickerModal)
        }
    }

    @Test
    fun `update tag selection when adding tag`() = runTest {
        viewModel = get(parameters = { parametersOf(null, null, null) })

        val tag = Tag.User(name = "tag")
        viewModel.handleIntent(EntryFormIntent.AddTag(tag))

        viewModel.tagSelection.test {
            assertTrue(awaitItem().contains(tag))
        }
    }

    @Test
    fun `update tag selection when removing tag`() = runTest {
        viewModel = get(parameters = { parametersOf(null, null, null) })

        val tag = Tag.User(name = "tag")
        viewModel.handleIntent(EntryFormIntent.AddTag(tag))
        viewModel.handleIntent(EntryFormIntent.RemoveTag(tag))

        viewModel.tagSelection.test {
            assertFalse(awaitItem().contains(tag))
        }
    }

    @Test
    fun `update measurements when editing input`() = runTest {
        viewModel = get(parameters = { parametersOf(null, null, null) })

        viewModel.state.test {
            awaitItem()

            val input = awaitItem()
                .measurements.first()
                .propertyInputStates.first()
                .copy(input = "10")
            viewModel.handleIntent(EntryFormIntent.Edit(input))

            assertEquals(
                expected = input,
                actual = awaitItem().measurements.first().propertyInputStates.first(),
            )
        }
    }

    @Test
    fun `store entry and pop screen when submitting with success`() = runTest {
        viewModel = get(parameters = { parametersOf(null, null, null) })

        turbineScope {
            val state = viewModel.state.testIn(backgroundScope)
            val navigation = navigation.events.testIn(backgroundScope)

            state.awaitItem()

            val input = state.awaitItem()
                .measurements.first()
                .propertyInputStates.first()
                .copy(input = "10")
            viewModel.handleIntent(EntryFormIntent.Edit(input))

            state.awaitItem()

            viewModel.handleIntent(EntryFormIntent.Submit)

            val values = valueRepository.getByEntryId(entryRepository.getLastId()!!)
            assertEquals(
                expected = 10.0,
                actual = values.first().value,
            )
            assertTrue(navigation.awaitItem() is NavigationEvent.PopScreen)
        }
    }

    @Test
    fun `show error in snackbar when missing input`() = runTest {
        viewModel = get(parameters = { parametersOf(null, null, null) })

        navigation.events.test {
            viewModel.handleIntent(EntryFormIntent.Submit)

            val event = awaitItem()
            assertTrue(event is NavigationEvent.ShowSnackbar)
            assertEquals(
                expected = "entry_form_error_missing_input",
                actual = event.message,
            )
        }
    }

    @Test
    fun `show modal if deleting entry`() = runTest {
        storeValue(10.0, DatabaseKey.MeasurementProperty.BLOOD_SUGAR)
        val entryId = entryRepository.getLastId()!!
        viewModel = get(parameters = { parametersOf(entryId, null, null) })

        navigation.events.test {
            viewModel.handleIntent(EntryFormIntent.Delete)

            val event = awaitItem()
            assertTrue(event is NavigationEvent.OpenModal)
            assertTrue(event.modal is DeleteModal)
        }
    }

    @Test
    fun `pop screen if deleting non-existing entry`() = runTest {
        viewModel = get(parameters = { parametersOf(null, null, null) })

        navigation.events.test {
            viewModel.handleIntent(EntryFormIntent.Delete)
            assertTrue(awaitItem() is NavigationEvent.PopScreen)
        }
    }
}