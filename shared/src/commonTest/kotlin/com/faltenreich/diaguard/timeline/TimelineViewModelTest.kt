package com.faltenreich.diaguard.timeline

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import app.cash.turbine.test
import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.data.DatabaseKey
import com.faltenreich.diaguard.data.measurement.category.MeasurementCategoryRepository
import com.faltenreich.diaguard.data.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.entry.form.EntryFormScreen
import com.faltenreich.diaguard.entry.search.EntrySearchScreen
import com.faltenreich.diaguard.measurement.value.usecase.StoreMeasurementValueUseCase
import com.faltenreich.diaguard.data.navigation.Navigation
import com.faltenreich.diaguard.data.navigation.NavigationEvent
import com.faltenreich.diaguard.startup.seed.ImportSeedUseCase
import com.faltenreich.diaguard.timeline.canvas.chart.TimelineChartState
import com.faltenreich.diaguard.timeline.canvas.table.TimelineTableState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.koin.test.inject
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TimelineViewModelTest : TestSuite() {

    private val importSeed: ImportSeedUseCase by inject()
    private val storeValue: StoreMeasurementValueUseCase by inject()
    private val dateTimeFactory: DateTimeFactory by inject()
    private val navigation: Navigation by inject()
    private val categoryRepository: MeasurementCategoryRepository by inject()
    private val propertyRepository: MeasurementPropertyRepository by inject()

    private val canvasSize = Size(width = 100f, height = 100f)
    private val tableRowHeight = 10f
    private val statusBarHeight = 10

    private val viewModel: TimelineViewModel by inject()

    @BeforeTest
    override fun beforeTest() {
        super.beforeTest()
        viewModel.dispatchIntent(
            TimelineIntent.Setup(
                canvasSize = canvasSize,
                tableRowHeight = tableRowHeight,
                statusBarHeight = statusBarHeight,
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

        val categories = categoryRepository.observeAll().first()
        val properties = propertyRepository.getAll()

        viewModel.state.test {
            val state = awaitItem()

            assertEquals(
                expected = TimelineChartState(
                    chartRectangle = Rect(0.0f, 10.0f, 100.0f, -20.0f),
                    iconRectangle = Rect(0.0f, -30.0f, 10.0f, -20.0f),
                    property = properties.first { it.key == DatabaseKey.MeasurementProperty.BLOOD_SUGAR },
                    items = emptyList(),
                    colorStops = listOf(
                        TimelineChartState.ColorStop(
                            offset = -0.20000005f,
                            type = TimelineChartState.ColorStop.Type.HIGH,
                        ),
                        TimelineChartState.ColorStop(
                            offset = -0.20000005f,
                            type = TimelineChartState.ColorStop.Type.NORMAL,
                        ),
                        TimelineChartState.ColorStop(
                            offset = 0.6f,
                            type = TimelineChartState.ColorStop.Type.NORMAL,
                        ),
                        TimelineChartState.ColorStop(
                            offset = 0.6f,
                            type = TimelineChartState.ColorStop.Type.LOW,
                        )
                    ),
                    labels = listOf(
                        TimelineChartState.Label(
                            position = Offset(0.0f, -13.0f),
                            text = "50",
                        ),
                        TimelineChartState.Label(
                            position = Offset(0.0f, -6.0f),
                            text = "100",
                        ),
                        TimelineChartState.Label(
                            position = Offset(0.0f, 1.0f),
                            text = "150",
                        ),
                    ),
                ),
                actual = state.canvas?.chart,
            )
            assertEquals(
                expected = TimelineTableState(
                    rectangle = Rect(0.0f, -20.0f, 100.0f, 90.0f),
                    categories = listOf(
                        TimelineTableState.Category(
                            category = categories.first { it.key == DatabaseKey.MeasurementCategory.INSULIN },
                            properties = listOf(
                                TimelineTableState.Property(
                                    property = properties.first {
                                        it.key == DatabaseKey.MeasurementProperty.INSULIN_BOLUS
                                    },
                                    rectangle = Rect(0.0f, -20.0f, 100.0f, -10.0f),
                                    iconRectangle = Rect(0.0f, -20.0f, 10.0f, -10.0f),
                                    name = "bolus",
                                    values = emptyList()
                                ),
                                TimelineTableState.Property(
                                    property = properties.first {
                                        it.key == DatabaseKey.MeasurementProperty.INSULIN_CORRECTION
                                    },
                                    rectangle = Rect(0.0f, -10.0f, 100.0f, 0.0f),
                                    iconRectangle = Rect(0.0f, -10.0f, 10.0f, 0.0f),
                                    name = "correction",
                                    values = emptyList(),
                                ),
                                TimelineTableState.Property(
                                    property = properties.first {
                                        it.key == DatabaseKey.MeasurementProperty.INSULIN_BASAL
                                    },
                                    rectangle = Rect(0.0f, 0.0f, 100.0f, 10.0f),
                                    iconRectangle = Rect(0.0f, 0.0f, 10.0f, 10.0f),
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
                                    rectangle = Rect(0.0f, 10.0f, 100.0f, 20.0f),
                                    iconRectangle = Rect(0.0f, 10.0f, 10.0f, 20.0f),
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
                                    rectangle = Rect(0.0f, 20.0f, 100.0f, 30.0f),
                                    iconRectangle = Rect(0.0f, 20.0f, 10.0f, 30.0f),
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
                                    rectangle = Rect(0.0f, 30.0f, 100.0f, 40.0f),
                                    iconRectangle = Rect(0.0f, 30.0f, 10.0f, 40.0f),
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
                                    rectangle = Rect(0.0f, 40.0f, 100.0f, 50.0f),
                                    iconRectangle = Rect(0.0f, 40.0f, 10.0f, 50.0f),
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
                                    rectangle = Rect(0.0f, 50.0f, 100.0f, 60.0f),
                                    iconRectangle = Rect(0.0f, 50.0f, 10.0f, 60.0f),
                                    name = "pulse",
                                    values = emptyList(),
                                ),
                            ),
                        ),
                        TimelineTableState.Category(
                            category = categories.first { it.key == DatabaseKey.MeasurementCategory.BLOOD_PRESSURE },
                            properties = listOf(
                                TimelineTableState.Property(
                                    property = properties.first {
                                        it.key == DatabaseKey.MeasurementProperty.BLOOD_PRESSURE_SYSTOLIC
                                    },
                                    rectangle = Rect(0.0f, 60.0f, 100.0f, 70.0f),
                                    iconRectangle = Rect(0.0f, 60.0f, 10.0f, 70.0f),
                                    name = "systolic",
                                    values = emptyList(),
                                ),
                                TimelineTableState.Property(
                                    property = properties.first {
                                        it.key == DatabaseKey.MeasurementProperty.BLOOD_PRESSURE_DIASTOLIC
                                    },
                                    rectangle = Rect(0.0f, 70.0f, 100.0f, 80.0f),
                                    iconRectangle = Rect(0.0f, 70.0f, 10.0f, 80.0f),
                                    name = "diastolic",
                                    values = emptyList(),
                                ),
                            ),
                        ),
                        TimelineTableState.Category(
                            category = categories.first { it.key == DatabaseKey.MeasurementCategory.OXYGEN_SATURATION },
                            properties = listOf(
                                TimelineTableState.Property(
                                    property = properties.first {
                                        it.key == DatabaseKey.MeasurementProperty.OXYGEN_SATURATION
                                    },
                                    rectangle = Rect(0.0f, 80.0f, 100.0f, 90.0f),
                                    iconRectangle = Rect(0.0f, 80.0f, 10.0f, 90.0f),
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

        viewModel.events.test {
            viewModel.handleIntent(TimelineIntent.SelectPreviousDate)

            val event = awaitItem()
            assertTrue(event is TimelineEvent.Scroll)
            assertEquals(
                expected = canvasSize.width,
                actual = event.offset,
            )
        }
    }

    @Test
    fun `forward next date when intending to move day forward`() = runTest {
        importSeed()

        viewModel.events.test {
            viewModel.handleIntent(TimelineIntent.SelectNextDate)

            val event = awaitItem()
            assertTrue(event is TimelineEvent.Scroll)
            assertEquals(
                expected = -canvasSize.width,
                actual = event.offset,
            )
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