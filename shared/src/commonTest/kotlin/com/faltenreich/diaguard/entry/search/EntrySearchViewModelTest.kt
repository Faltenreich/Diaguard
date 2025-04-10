package com.faltenreich.diaguard.entry.search

import app.cash.turbine.test
import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.entry.form.EntryFormScreen
import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.navigation.NavigationEvent
import kotlinx.coroutines.test.runTest
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertTrue

class EntrySearchViewModelTest : TestSuite {

    private val navigation: Navigation by inject()
    private val entryRepository: EntryRepository by inject()
    private val dateTimeFactory: DateTimeFactory by inject()

    private val viewModel: EntrySearchViewModel by inject(parameters = { parametersOf("") })

    @Test
    @Ignore
    fun `push screen when opening entry`() = runTest {
        // FIXME: lateinit property pagingSource has not been initialized
        navigation.events.test {
            val entryId = entryRepository.create(
                Entry.User(
                    dateTime = dateTimeFactory.now(),
                    note = null,
                )
            )
            val entry = entryRepository.getById(entryId)!!
            viewModel.handleIntent(EntrySearchIntent.OpenEntry(entry))

            val event = awaitItem()
            assertTrue(event is NavigationEvent.PushScreen)
            assertTrue(event.screen is EntryFormScreen)
        }
    }
}