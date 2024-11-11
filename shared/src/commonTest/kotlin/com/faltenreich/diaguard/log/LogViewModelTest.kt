package com.faltenreich.diaguard.log

import app.cash.turbine.test
import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.datetime.picker.DatePickerModal
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.entry.form.EntryFormScreen
import com.faltenreich.diaguard.entry.search.EntrySearchScreen
import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.navigation.NavigationEvent
import kotlinx.coroutines.test.runTest
import org.koin.core.component.inject
import kotlin.test.Test
import kotlin.test.assertTrue

class LogViewModelTest : TestSuite {

    private val viewModel: LogViewModel by inject()
    private val navigation: Navigation by inject()
    private val entryRepository: EntryRepository by inject()
    private val dateTimeFactory: DateTimeFactory by inject()

    @Test
    fun `push screen when creating entry`() = runTest {
        navigation.events.test {
            val date = dateTimeFactory.now().date
            viewModel.handleIntent(LogIntent.CreateEntry(date))

            val event = awaitItem()
            assertTrue(event is NavigationEvent.PushScreen)
            assertTrue(event.screen is EntryFormScreen)
        }
    }

    @Test
    fun `push screen when opening entry`() = runTest {
        navigation.events.test {
            val entryId = entryRepository.create(
                Entry.User(
                    dateTime = dateTimeFactory.now(),
                    note = null,
                )
            )
            val entry = entryRepository.getById(entryId)!!
            viewModel.handleIntent(LogIntent.OpenEntry(entry))

            val event = awaitItem()
            assertTrue(event is NavigationEvent.PushScreen)
            assertTrue(event.screen is EntryFormScreen)
        }
    }

    @Test
    fun `push screen when opening entry search`() = runTest {
        navigation.events.test {
            viewModel.handleIntent(LogIntent.OpenEntrySearch(query = ""))

            val event = awaitItem()
            assertTrue(event is NavigationEvent.PushScreen)
            assertTrue(event.screen is EntrySearchScreen)
        }
    }

    @Test
    fun `open modal when selecting date`() = runTest {
        navigation.events.test {
            viewModel.handleIntent(LogIntent.SelectDate)

            val event = awaitItem()
            assertTrue(event is NavigationEvent.OpenModal)
            assertTrue(event.modal is DatePickerModal)
        }
    }
}