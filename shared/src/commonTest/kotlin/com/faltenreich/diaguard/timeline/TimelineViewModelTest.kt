package com.faltenreich.diaguard.timeline

import app.cash.turbine.test
import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.entry.form.EntryFormScreen
import com.faltenreich.diaguard.entry.search.EntrySearchScreen
import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.navigation.NavigationEvent
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.timeline.canvas.chart.TimelineChartState
import com.faltenreich.diaguard.timeline.canvas.table.TimelineTableState
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
    fun `launch with current date of today`() = runTest {
        viewModel.state.test {
            assertEquals(
                expected = dateTimeFactory.today(),
                actual = awaitItem().date.currentDate,
            )
        }
    }

    @Test
    fun `return data with seed categories`() = runTest {
        importSeed()

        viewModel.state.test {
            val state = awaitItem()

            assertEquals(
                expected = TimelineChartState(
                    values = emptyList(),
                    valueMin = 0.0,
                    valueLow = 60.0,
                    valueHigh = 180.0,
                    valueMax = 250.0,
                    valueStep = 50.0,
                ),
                actual = state.chart,
            )
            assertEquals(
                expected = TimelineTableState(
                    categories = listOf(
                        TimelineTableState.Category(
                            icon = "💉",
                            name = "insulin",
                            properties = listOf(
                                TimelineTableState.Property(
                                    name = "bolus",
                                    values = emptyList()
                                ),
                                TimelineTableState.Property(
                                    name = "correction",
                                    values = emptyList(),
                                ),
                                TimelineTableState.Property(
                                    name = "basal",
                                    values = emptyList(),
                                ),
                            ),
                        ),
                        TimelineTableState.Category(
                            icon = "🍞",
                            name = "meal",
                            properties = listOf(
                                TimelineTableState.Property(
                                    name = "meal",
                                    values = emptyList(),
                                ),
                            ),
                        ),
                        TimelineTableState.Category(
                            icon = "🏃",
                            name = "activity",
                            properties = listOf(
                                TimelineTableState.Property(
                                    name = "activity",
                                    values = emptyList(),
                                ),
                            ),
                        ),
                        TimelineTableState.Category(
                            icon = "%",
                            name = "hba1c",
                            properties = listOf(
                                TimelineTableState.Property(
                                    name = "hba1c",
                                    values = emptyList(),
                                ),
                            ),
                        ),
                        TimelineTableState.Category(
                            icon = "🏋",
                            name = "weight",
                            properties = listOf(
                                TimelineTableState.Property(
                                    name = "weight",
                                    values = emptyList(),
                                ),
                            ),
                        ),
                        TimelineTableState.Category(
                            icon = "💚",
                            name = "pulse",
                            properties = listOf(
                                TimelineTableState.Property(
                                    name = "pulse",
                                    values = emptyList(),
                                ),
                            ),
                        ),
                        TimelineTableState.Category(
                            icon = "⛽",
                            name = "blood_pressure",
                            properties = listOf(
                                TimelineTableState.Property(
                                    name = "systolic",
                                    values = emptyList(),
                                ),
                                TimelineTableState.Property(
                                    name = "diastolic",
                                    values = emptyList(),
                                ),
                            ),
                        ),
                        TimelineTableState.Category(
                            icon = "O²",
                            name = "oxygen_saturation",
                            properties = listOf(
                                TimelineTableState.Property(
                                    name = "oxygen_saturation",
                                    values = emptyList(),
                                ),
                            ),
                        ),
                    ),
                ),
                actual = state.table,
            )
        }
    }

    @Test
    fun `return chart data with values of blood sugar`() = runTest {
        importSeed()

        storeValue(120.0, DatabaseKey.MeasurementProperty.BLOOD_SUGAR)
        storeValue(20.0, DatabaseKey.MeasurementProperty.ACTIVITY)

        viewModel.state.test {
            assertEquals(
                expected = 1,
                actual = awaitItem().chart.values.size,
            )
        }
    }

    @Test
    fun `return table data with values other than blood sugar`() = runTest {
        importSeed()

        storeValue(120.0, DatabaseKey.MeasurementProperty.BLOOD_SUGAR)
        storeValue(20.0, DatabaseKey.MeasurementProperty.ACTIVITY)

        viewModel.state.test {
            assertEquals(
                expected = 1,
                actual = awaitItem().table
                    .categories.first { it.properties.any { it.name == "activity" } }
                    .properties.first().values.size,
            )
        }
    }

    @Test
    fun `update date when intending to setting current date`() = runTest {
        viewModel.state.test {
            val date = dateTimeFactory.date(1970, 1, 1)

            viewModel.handleIntent(TimelineIntent.Invalidate(date))

            assertEquals(
                expected = date,
                actual = awaitItem().date.currentDate,
            )
        }
    }

    @Test
    fun `forward previous date when intending to move day back`() = runTest {
        viewModel.state.test {
            val currentDate = awaitItem().date.currentDate

            viewModel.handleIntent(TimelineIntent.SelectPreviousDate)

            viewModel.events.test {
                val event = awaitItem()
                assertTrue(event is TimelineEvent.Scroll)
                assertEquals(
                    expected = currentDate.minus(1, DateUnit.DAY),
                    actual = event.date,
                )
            }
        }
    }

    @Test
    fun `forward next date when intending to move day forward`() = runTest {
        viewModel.state.test {
            val currentDate = awaitItem().date.currentDate

            viewModel.handleIntent(TimelineIntent.SelectNextDate)

            viewModel.events.test {
                val event = awaitItem()
                assertTrue(event is TimelineEvent.Scroll)
                assertEquals(
                    expected = currentDate.plus(1, DateUnit.DAY),
                    actual = event.date,
                )
            }
        }
    }

    @Test
    fun `open screen when intending to create entry`() = runTest {
        navigation.events.test {
            viewModel.handleIntent(TimelineIntent.CreateEntry)

            val event = awaitItem()
            assertTrue(event is NavigationEvent.PushScreen)
            assertTrue(event.screen is EntryFormScreen)
        }
    }

    @Test
    fun `open screen when intending to search entries`() = runTest {
        navigation.events.test {
            viewModel.handleIntent(TimelineIntent.SearchEntries)

            val event = awaitItem()
            assertTrue(event is NavigationEvent.PushScreen)
            assertTrue(event.screen is EntrySearchScreen)
        }
    }
}