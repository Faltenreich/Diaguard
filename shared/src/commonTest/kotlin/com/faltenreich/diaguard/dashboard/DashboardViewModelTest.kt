package com.faltenreich.diaguard.dashboard

import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.shared.database.DatabaseKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DashboardViewModelTest : TestSuite {

    private val viewModel: DashboardViewModel by inject()

    @Test
    fun `state contains empty content if no data is available`() = runTest {
        val state = viewModel.state.first()

        assertNull(state.latestBloodSugar)

        assertEquals(expected = 0, actual = state.today.totalCount)
        assertEquals(expected = 0, actual = state.today.hypoCount)
        assertEquals(expected = 0, actual = state.today.hyperCount)

        assertNull(state.average.day)
        assertNull(state.average.week)
        assertNull(state.average.month)

        assertNull(state.hbA1c.value)

        assertTrue(state.trend.values.isEmpty())
    }

    @Test
    fun `state contains content if data is available`() = runTest {
        importSeed()
        storeValue(value = 50.0, propertyKey = DatabaseKey.MeasurementProperty.BLOOD_SUGAR)
        storeValue(value = 120.0, propertyKey = DatabaseKey.MeasurementProperty.BLOOD_SUGAR)
        storeValue(value = 190.0, propertyKey = DatabaseKey.MeasurementProperty.BLOOD_SUGAR)

        val state = viewModel.state.first()

        assertNotNull(state.latestBloodSugar)

        assertEquals(expected = 3, actual = state.today.totalCount)
        assertEquals(expected = 1, actual = state.today.hypoCount)
        assertEquals(expected = 1, actual = state.today.hyperCount)

        assertNotNull(state.average.day)
        assertNotNull(state.average.week)
        assertNotNull(state.average.month)

        assertNotNull(state.hbA1c.value)

        // TODO: assertTrue(state.trend.values.isNotEmpty())
    }
}