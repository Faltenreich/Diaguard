package com.faltenreich.diaguard.timeline

import app.cash.turbine.test
import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
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
    private val dateTimeFactory: DateTimeFactory by inject()
    private val navigation: Navigation by inject()

    @Test
    fun `launches with current date of today`() = runTest {
        viewModel.state.test {
            assertEquals(
                expected = dateTimeFactory.today(),
                actual = awaitItem().currentDate,
            )
        }
    }

    @Test
    fun `returns data with seed categories for timeline`() = runTest {
        importSeed()

        viewModel.state.test {
            assertEquals(
                expected = TimelineData(
                    chart = TimelineData.Chart(
                        values = emptyList(),
                    ),
                    table = TimelineData.Table(
                        categories = listOf(
                            TimelineData.Table.Category(
                                properties = listOf(
                                    TimelineData.Table.Category.Property(
                                        icon ="🩸",
                                        name = "blood_sugar",
                                        unit = "blood_sugar",
                                        values = emptyList()
                                    ),
                                ),
                            ),
                            TimelineData.Table.Category(
                                properties = listOf(
                                    TimelineData.Table.Category.Property(
                                        icon = "💉",
                                        name = "insulin",
                                        unit = "bolus",
                                        values = emptyList()
                                    ),
                                    TimelineData.Table.Category.Property(
                                        icon = "💉",
                                        name = "insulin",
                                        unit = "correction",
                                        values = emptyList(),
                                    ),
                                    TimelineData.Table.Category.Property(
                                        icon = "💉",
                                        name = "insulin",
                                        unit = "basal",
                                        values = emptyList(),
                                    ),
                                ),
                            ),
                            TimelineData.Table.Category(
                                properties = listOf(
                                    TimelineData.Table.Category.Property(
                                        icon = "🍞",
                                        name = "meal",
                                        unit = "meal",
                                        values = emptyList(),
                                    ),
                                ),
                            ),
                            TimelineData.Table.Category(
                                properties = listOf(
                                    TimelineData.Table.Category.Property(
                                        icon = "🏃",
                                        name = "activity",
                                        unit = "activity",
                                        values = emptyList(),
                                    ),
                                ),
                            ),
                            TimelineData.Table.Category(
                                properties = listOf(
                                    TimelineData.Table.Category.Property(
                                        icon = "%",
                                        name = "hba1c",
                                        unit = "hba1c",
                                        values = emptyList(),
                                    ),
                                ),
                            ),
                            TimelineData.Table.Category(
                                properties = listOf(
                                    TimelineData.Table.Category.Property(
                                        icon = "🏋",
                                        name = "weight",
                                        unit = "weight",
                                        values = emptyList(),
                                    ),
                                ),
                            ),
                            TimelineData.Table.Category(
                                properties = listOf(
                                    TimelineData.Table.Category.Property(
                                        icon = "💚",
                                        name = "pulse",
                                        unit = "pulse",
                                        values = emptyList(),
                                    ),
                                ),
                            ),
                            TimelineData.Table.Category(
                                properties = listOf(
                                    TimelineData.Table.Category.Property(
                                        icon = "⛽",
                                        name = "blood_pressure",
                                        unit = "systolic",
                                        values = emptyList(),
                                    ),
                                    TimelineData.Table.Category.Property(
                                        icon = "⛽",
                                        name = "blood_pressure",
                                        unit = "diastolic",
                                        values = emptyList(),
                                    ),
                                ),
                            ),
                            TimelineData.Table.Category(
                                properties = listOf(
                                    TimelineData.Table.Category.Property(
                                        icon = "O²",
                                        name = "oxygen_saturation",
                                        unit = "oxygen_saturation",
                                        values = emptyList(),
                                    ),
                                ),
                            ),
                        ),
                    ),
                ),
                actual = awaitItem().data,
            )
        }
    }

    @Test
    fun `updates date when intending to setting current date`() = runTest {
        viewModel.state.test {
            val date = dateTimeFactory.date(1970, 1, 1)

            viewModel.handleIntent(TimelineIntent.SetCurrentDate(date))

            assertEquals(
                expected = date,
                actual = awaitItem().currentDate,
            )
        }
    }

    @Test
    fun `forwards previous date when intending to move day back`() = runTest {
        viewModel.state.test {
            val currentDate = awaitItem().currentDate

            viewModel.handleIntent(TimelineIntent.MoveDayBack)

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

            viewModel.handleIntent(TimelineIntent.MoveDayForward)

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