package com.faltenreich.diaguard.timeline

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import app.cash.turbine.test
import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.entry.form.EntryFormScreen
import com.faltenreich.diaguard.entry.search.EntrySearchScreen
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryRepository
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.navigation.NavigationEvent
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.timeline.canvas.chart.TimelineChartState
import com.faltenreich.diaguard.timeline.canvas.table.TimelineTableState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TimelineViewModelTest : TestSuite {

    private val viewModel: TimelineViewModel by inject()
    private val dateTimeFactory: DateTimeFactory by inject()
    private val navigation: Navigation by inject()
    private val categoryRepository: MeasurementCategoryRepository by inject()
    private val propertyRepository: MeasurementPropertyRepository by inject()

    private suspend fun setup() {
        viewModel.handleIntent(
            TimelineIntent.Setup(
                canvasSize = Size(width = 100f, height = 100f),
                tableRowHeight = 10f,
                statusBarHeight = 10,
            )
        )
    }

    @Test
    fun `launch with current date of today`() = runTest {
        importSeed()

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
        setup()

        val categories = categoryRepository.observeAll().first()
        val properties = propertyRepository.getAll()

        viewModel.state.test {
            val state = awaitItem()

            assertEquals(
                expected = TimelineChartState(
                    chartRectangle = Rect.Zero,
                    iconRectangle = Rect.Zero,
                    property = properties.first { it.key == DatabaseKey.MeasurementProperty.BLOOD_SUGAR },
                    items = emptyList(),
                    colorStops = emptyList(),
                    labels = listOf(),
                ),
                actual = state.canvas?.chart,
            )
            assertEquals(
                expected = TimelineTableState(
                    rectangle = Rect.Zero,
                    categories = listOf(
                        TimelineTableState.Category(
                            category = categories.first { it.key == DatabaseKey.MeasurementCategory.INSULIN },
                            properties = listOf(
                                TimelineTableState.Property(
                                    property = properties.first { it.key == DatabaseKey.MeasurementProperty.INSULIN_BOLUS },
                                    rectangle = Rect.Zero,
                                    iconRectangle = Rect.Zero,
                                    name = "bolus",
                                    values = emptyList()
                                ),
                                TimelineTableState.Property(
                                    property = properties.first { it.key == DatabaseKey.MeasurementProperty.INSULIN_CORRECTION },
                                    rectangle = Rect.Zero,
                                    iconRectangle = Rect.Zero,
                                    name = "correction",
                                    values = emptyList(),
                                ),
                                TimelineTableState.Property(
                                    property = properties.first { it.key == DatabaseKey.MeasurementProperty.INSULIN_BASAL },
                                    rectangle = Rect.Zero,
                                    iconRectangle = Rect.Zero,
                                    name = "basal",
                                    values = emptyList(),
                                ),
                            ),
                        ),
                        TimelineTableState.Category(
                            category = categories.first { it.key == DatabaseKey.MeasurementCategory.MEAL },
                            properties = listOf(
                                TimelineTableState.Property(
                                    property = properties.first { it.key == DatabaseKey.MeasurementProperty.MEAL },
                                    rectangle = Rect.Zero,
                                    iconRectangle = Rect.Zero,
                                    name = "meal",
                                    values = emptyList(),
                                ),
                            ),
                        ),
                        TimelineTableState.Category(
                            category = categories.first { it.key == DatabaseKey.MeasurementCategory.ACTIVITY },
                            properties = listOf(
                                TimelineTableState.Property(
                                    property = properties.first { it.key == DatabaseKey.MeasurementProperty.ACTIVITY },
                                    rectangle = Rect.Zero,
                                    iconRectangle = Rect.Zero,
                                    name = "activity",
                                    values = emptyList(),
                                ),
                            ),
                        ),
                        TimelineTableState.Category(
                            category = categories.first { it.key == DatabaseKey.MeasurementCategory.HBA1C },
                            properties = listOf(
                                TimelineTableState.Property(
                                    property = properties.first { it.key == DatabaseKey.MeasurementProperty.HBA1C },
                                    rectangle = Rect.Zero,
                                    iconRectangle = Rect.Zero,
                                    name = "hba1c",
                                    values = emptyList(),
                                ),
                            ),
                        ),
                        TimelineTableState.Category(
                            category = categories.first { it.key == DatabaseKey.MeasurementCategory.WEIGHT },
                            properties = listOf(
                                TimelineTableState.Property(
                                    property = properties.first { it.key == DatabaseKey.MeasurementProperty.WEIGHT },
                                    rectangle = Rect.Zero,
                                    iconRectangle = Rect.Zero,
                                    name = "weight",
                                    values = emptyList(),
                                ),
                            ),
                        ),
                        TimelineTableState.Category(
                            category = categories.first { it.key == DatabaseKey.MeasurementCategory.PULSE },
                            properties = listOf(
                                TimelineTableState.Property(
                                    property = properties.first { it.key == DatabaseKey.MeasurementProperty.PULSE },
                                    rectangle = Rect.Zero,
                                    iconRectangle = Rect.Zero,
                                    name = "pulse",
                                    values = emptyList(),
                                ),
                            ),
                        ),
                        TimelineTableState.Category(
                            category = categories.first { it.key == DatabaseKey.MeasurementCategory.BLOOD_PRESSURE },
                            properties = listOf(
                                TimelineTableState.Property(
                                    property = properties.first { it.key == DatabaseKey.MeasurementProperty.BLOOD_PRESSURE_SYSTOLIC },
                                    rectangle = Rect.Zero,
                                    iconRectangle = Rect.Zero,
                                    name = "systolic",
                                    values = emptyList(),
                                ),
                                TimelineTableState.Property(
                                    property = properties.first { it.key == DatabaseKey.MeasurementProperty.BLOOD_PRESSURE_DIASTOLIC },
                                    rectangle = Rect.Zero,
                                    iconRectangle = Rect.Zero,
                                    name = "diastolic",
                                    values = emptyList(),
                                ),
                            ),
                        ),
                        TimelineTableState.Category(
                            category = categories.first { it.key == DatabaseKey.MeasurementCategory.OXYGEN_SATURATION },
                            properties = listOf(
                                TimelineTableState.Property(
                                    property = properties.first { it.key == DatabaseKey.MeasurementProperty.OXYGEN_SATURATION },
                                    rectangle = Rect.Zero,
                                    iconRectangle = Rect.Zero,
                                    name = "oxygen_saturation",
                                    values = emptyList(),
                                ),
                            ),
                        ),
                    ),
                ),
                actual = state.canvas?.table,
            )
        }
    }

    @Test
    fun `return chart data with values of blood sugar`() = runTest {
        importSeed()
        setup()

        storeValue(120.0, DatabaseKey.MeasurementProperty.BLOOD_SUGAR)
        storeValue(20.0, DatabaseKey.MeasurementProperty.ACTIVITY)

        viewModel.state.test {
            assertEquals(
                expected = 1,
                actual = awaitItem().canvas?.chart?.items?.size,
            )
        }
    }

    @Test
    fun `return table data with values other than blood sugar`() = runTest {
        importSeed()
        setup()

        storeValue(120.0, DatabaseKey.MeasurementProperty.BLOOD_SUGAR)
        storeValue(20.0, DatabaseKey.MeasurementProperty.ACTIVITY)

        viewModel.state.test {
            assertEquals(
                expected = 1,
                actual = awaitItem().canvas?.table
                    ?.categories?.first { it.properties.any { it.name == "activity" } }
                    ?.properties?.first()?.values?.size,
            )
        }
    }

    @Test
    fun `forward previous date when intending to move day back`() = runTest {
        importSeed()
        setup()

        viewModel.state.test {
            val currentDate = awaitItem().date.currentDate

            viewModel.handleIntent(TimelineIntent.SelectPreviousDate)

            viewModel.events.test {
                val event = awaitItem()
                assertTrue(event is TimelineEvent.Scroll)
                assertEquals(
                    expected = -1080f,
                    actual = event.offset,
                )
            }
        }
    }

    @Test
    fun `forward next date when intending to move day forward`() = runTest {
        importSeed()
        setup()

        viewModel.state.test {
            val currentDate = awaitItem().date.currentDate

            viewModel.handleIntent(TimelineIntent.SelectNextDate)

            viewModel.events.test {
                val event = awaitItem()
                assertTrue(event is TimelineEvent.Scroll)
                assertEquals(
                    expected = 1080f,
                    actual = event.offset,
                )
            }
        }
    }

    @Test
    fun `open screen when intending to create entry`() = runTest {
        importSeed()

        navigation.events.test {
            viewModel.handleIntent(TimelineIntent.CreateEntry)

            val event = awaitItem()
            assertTrue(event is NavigationEvent.PushScreen)
            assertTrue(event.screen is EntryFormScreen)
        }
    }

    @Test
    fun `open screen when intending to search entries`() = runTest {
        importSeed()

        navigation.events.test {
            viewModel.handleIntent(TimelineIntent.OpenEntrySearch())

            val event = awaitItem()
            assertTrue(event is NavigationEvent.PushScreen)
            assertTrue(event.screen is EntrySearchScreen)
        }
    }
}