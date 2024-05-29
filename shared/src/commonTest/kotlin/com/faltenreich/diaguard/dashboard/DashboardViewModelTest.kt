package com.faltenreich.diaguard.dashboard

import app.cash.turbine.test
import com.faltenreich.diaguard.DependencyInjection
import com.faltenreich.diaguard.appModules
import com.faltenreich.diaguard.backup.ImportUseCase
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.entry.form.CreateEntryUseCase
import com.faltenreich.diaguard.entry.form.EntryFormInput
import com.faltenreich.diaguard.entry.form.measurement.MeasurementCategoryInputState
import com.faltenreich.diaguard.entry.form.measurement.MeasurementPropertyInputState
import com.faltenreich.diaguard.measurement.category.form.GetMeasurementPropertiesUseCase
import com.faltenreich.diaguard.testModules
import kotlinx.coroutines.test.runTest
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class DashboardViewModelTest : KoinTest {

    private val import: ImportUseCase by inject()
    private val getProperties: GetMeasurementPropertiesUseCase by inject()
    private val createEntry: CreateEntryUseCase by inject()
    private val dateTimeFactory: DateTimeFactory by inject()

    private val viewModel: DashboardViewModel by inject()

    @BeforeTest
    fun setup() {
        DependencyInjection.setup(modules = appModules() + testModules())
    }

    @Test
    fun `state contains null content if no data is available`() = runTest {
        viewModel.state.test {
            assertEquals(
                awaitItem(),
                DashboardViewState(
                    latestBloodSugar = null,
                    today = null,
                    average = null,
                    hbA1c = null,
                    trend = null,
                ),
            )
            awaitComplete()
        }
    }

    @Test
    fun `state contains blank content if seed data is available`() = runTest {
        import()
        viewModel.state.test {
            assertEquals(
                awaitItem(),
                DashboardViewState(
                    latestBloodSugar = null,
                    today = DashboardViewState.Today(
                        totalCount = 0,
                        hypoCount = 0,
                        hyperCount = 0,
                    ),
                    average = DashboardViewState.Average(
                        day = null,
                        week = null,
                        month = null,
                    ),
                    hbA1c = null,
                    trend = null,
                ),
            )
            awaitComplete()
        }
    }

    @Test
    fun `state contains latest blood sugar if blood sugar is available`() = runTest {
        import()

        val bloodSugar = MeasurementCategoryInputState(
            category = TODO(),
            propertyInputStates = listOf(
                MeasurementPropertyInputState(
                    property = TODO(),
                    input = "100",
                    isLast = true,
                    error = null,
                )
            ),
            error = null,
        )
        val entry = EntryFormInput(
            id = null,
            dateTime = dateTimeFactory.now(),
            measurements = listOf(bloodSugar),
            tags = emptyList(),
            note = null,
            foodEaten = emptyList(),
        )
        createEntry(entry)

        viewModel.state.test {
            assertNotNull(awaitItem().latestBloodSugar)
            awaitComplete()
        }
    }
}