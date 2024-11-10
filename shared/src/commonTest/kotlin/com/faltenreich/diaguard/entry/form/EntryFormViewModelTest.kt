package com.faltenreich.diaguard.entry.form

import app.cash.turbine.test
import app.cash.turbine.turbineScope
import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.datetime.picker.DatePickerModal
import com.faltenreich.diaguard.datetime.picker.TimePickerModal
import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.navigation.NavigationEvent
import com.faltenreich.diaguard.tag.Tag
import kotlinx.coroutines.test.runTest
import org.koin.core.parameter.parametersOf
import org.koin.test.inject
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class EntryFormViewModelTest : TestSuite {

    private val viewModel: EntryFormViewModel by inject(
        parameters = { parametersOf(null, null, null) },
    )
    private val navigation: Navigation by inject()

    @BeforeTest
    override fun beforeTest() {
        super.beforeTest()
        importSeed()
    }

    @Test
    fun `launch with no and then some categories`() = runTest {
        viewModel.state.test {
            assertTrue(awaitItem().measurements.isEmpty())
            assertTrue(awaitItem().measurements.isNotEmpty())
        }
    }

    @Test
    fun `launch with tags`() = runTest {
        viewModel.state.test {
            assertTrue(awaitItem().tags.isNotEmpty())
        }
    }

    @Test
    fun `open dialog when selecting date`() = runTest {
        viewModel.handleIntent(EntryFormIntent.SelectDate)
        navigation.events.test {
            val event = awaitItem()
            assertTrue(event is NavigationEvent.OpenModal)
            assertTrue(event.modal is DatePickerModal)
        }
    }

    @Test
    fun `open dialog when selecting time`() = runTest {
        viewModel.handleIntent(EntryFormIntent.SelectTime)
        navigation.events.test {
            val event = awaitItem()
            assertTrue(event is NavigationEvent.OpenModal)
            assertTrue(event.modal is TimePickerModal)
        }
    }

    @Test
    fun `update tag selection when adding tag`() = runTest {
        val tag = Tag.User(name = "tag")
        viewModel.handleIntent(EntryFormIntent.AddTag(tag))

        viewModel.tagSelection.test {
            val tags = awaitItem()
            assertTrue(tags.contains(tag))
        }
    }

    @Test
    fun `update tag selection when removing tag`() = runTest {
        val tag = Tag.User(name = "tag")
        viewModel.handleIntent(EntryFormIntent.AddTag(tag))
        viewModel.handleIntent(EntryFormIntent.RemoveTag(tag))

        viewModel.tagSelection.test {
            val tags = awaitItem()
            assertFalse(tags.contains(tag))
        }
    }

    @Test
    fun `update measurements when editing input`() = runTest {
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
    fun `pop screen when submitting with success`() = runTest {
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

            assertTrue(navigation.awaitItem() is NavigationEvent.PopScreen)
        }
    }

    @Test
    fun `show error in snackbar when missing input`() = runTest {
        viewModel.handleIntent(EntryFormIntent.Submit)

        navigation.events.test {
            val event = awaitItem()
            assertTrue(event is NavigationEvent.ShowSnackbar)
            assertEquals(
                expected = "entry_form_error_missing_input",
                actual = event.message,
            )
        }
    }
}