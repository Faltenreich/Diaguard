package com.faltenreich.diaguard.timeline

import app.cash.turbine.test
import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.picker.DatePickerModal
import com.faltenreich.diaguard.entry.form.EntryFormScreen
import com.faltenreich.diaguard.entry.search.EntrySearchScreen
import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.navigation.NavigationEvent
import kotlinx.coroutines.test.runTest
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TimelineViewModelTest : TestSuite {

    private val viewModel: TimelineViewModel by inject()
    private val navigation: Navigation by inject()

    @Test
    fun `forwards previous date when intending to move day back`() = runTest {
        viewModel.state.test {
            val currentDate = awaitItem().currentDate

            viewModel.dispatchIntent(TimelineIntent.MoveDayBack)

            viewModel.events.test {
                val event = awaitItem()
                assertTrue(event is TimelineEvent.DateSelected)
                assertEquals(
                    expected = currentDate.minus(1, DateUnit.DAY),
                    actual = event.date,
                )
            }
        }
    }

    @Test
    fun `forwards next date when intending to move day forward`() = runTest {
        viewModel.state.test {
            val currentDate = awaitItem().currentDate

            viewModel.dispatchIntent(TimelineIntent.MoveDayForward)

            viewModel.events.test {
                val event = awaitItem()
                assertTrue(event is TimelineEvent.DateSelected)
                assertEquals(
                    expected = currentDate.plus(1, DateUnit.DAY),
                    actual = event.date,
                )
            }
        }
    }

    @Test
    fun `opens screen when intending to create entry`() = runTest {
        navigation.events.test {
            viewModel.handleIntent(TimelineIntent.CreateEntry)

            val event = awaitItem()
            assertTrue(event is NavigationEvent.PushScreen)
            assertTrue(event.screen is EntryFormScreen)
        }
    }

    @Test
    fun `opens screen when intending to search entries`() = runTest {
        navigation.events.test {
            viewModel.handleIntent(TimelineIntent.SearchEntries)

            val event = awaitItem()
            assertTrue(event is NavigationEvent.PushScreen)
            assertTrue(event.screen is EntrySearchScreen)
        }
    }

    @Test
    fun `opens modal when intending to show date picker`() = runTest {
        navigation.events.test {
            viewModel.handleIntent(TimelineIntent.ShowDatePicker)

            val event = awaitItem()
            assertTrue(event is NavigationEvent.OpenModal)
            assertTrue(event.modal is DatePickerModal)
        }
    }
}