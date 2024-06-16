package com.faltenreich.diaguard.dashboard

import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.backup.ImportUseCase
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.measurement.value.tint.MeasurementValueTint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertEquals

class DashboardViewModelTest : TestSuite {

    private val dateTimeFactory: DateTimeFactory by inject()
    private val import: ImportUseCase by inject()

    private val viewModel: DashboardViewModel by inject()

    @Test
    fun `state contains null content if no data is available`() = runTest {
        assertEquals(
            DashboardViewState(
                latestBloodSugar = null,
                today = null,
                average = null,
                hbA1c = null,
                trend = null,
            ),
            viewModel.state.first(),
        )
    }

    @Test
    fun `state contains blank content if seed data is available`() = runTest {
        import()

        assertEquals(
            DashboardViewState(
                latestBloodSugar = DashboardViewState.LatestBloodSugar(
                    entry = Entry.Local(
                        id= 0,
                        createdAt = dateTimeFactory.dateTime(isoString = "2024-06-08T18:46:38.200"),
                        updatedAt = dateTimeFactory.dateTime(isoString = "2024-06-08T18:46:38.200"),
                        dateTime = dateTimeFactory.dateTime(isoString = "2024-05-09T08:00"),
                        note = "Hello, World",
                    ),
                    value = "5,55",
                    tint = MeasurementValueTint.NORMAL,
                    timePassed = "date_time_ago_days",
                ),
                today = DashboardViewState.Today(
                    totalCount = 3,
                    hypoCount = 0,
                    hyperCount = 0,
                ),
                average = DashboardViewState.Average(
                    day = "5,92",
                    week = "5,92",
                    month = "5,92",
                ),
                hbA1c = null,
                trend = null,
            ),
            viewModel.state.first(),
        )
    }
}