package com.faltenreich.diaguard.dashboard

import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.shared.database.DatabaseKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class DashboardViewModelTest : TestSuite {

    private val viewModel: DashboardViewModel by inject()

    @Test
    fun `state contains empty content if no data is available`() = runTest {
        val state = viewModel.state.first()

        assertEquals(
            expected = DashboardState(
                latestBloodSugar = null,
                today = DashboardState.Today(
                    totalCount = 0,
                    hypoCount = 0,
                    hyperCount = 0,
                ),
                average = DashboardState.Average(
                    day = null,
                    week = null,
                    month = null,
                ),
                hbA1c = DashboardState.HbA1c(
                    label = "hba1c_latest",
                    value = null,
                    onClick = null,
                ),
                trend = DashboardState.Trend(
                    values = emptyMap(),
                ),
            ),
            actual = state,
        )
    }

    @Test
    fun `state contains content if data is available`() = runTest {
        importSeed()
        storeValue(value = 120.0, propertyKey = DatabaseKey.MeasurementProperty.BLOOD_SUGAR)

        val state = viewModel.state.first()

        assertNotNull(state.latestBloodSugar)

        assertTrue(state.today.totalCount > 0)

        assertNotNull(state.average.day)
        assertNotNull(state.average.week)
        assertNotNull(state.average.month)

        assertNotNull(state.hbA1c.value)

        // TODO: assertTrue(state.trend.values.isNotEmpty())
    }
}