package com.faltenreich.diaguard.dashboard

import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.backup.ImportUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertEquals

class DashboardViewModelTest : TestSuite {

    private val import: ImportUseCase by inject()

    private val viewModel: DashboardViewModel by inject()

    @Test
    fun `state contains null content if no data is available`() = runTest {
        assertEquals(
            viewModel.state.first(),
            DashboardViewState(
                latestBloodSugar = null,
                today = null,
                average = null,
                hbA1c = null,
                trend = null,
            ),
        )
    }

    @Test
    fun `state contains blank content if seed data is available`() = runTest {
        import()

        assertEquals(
            viewModel.state.first(),
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
    }
}