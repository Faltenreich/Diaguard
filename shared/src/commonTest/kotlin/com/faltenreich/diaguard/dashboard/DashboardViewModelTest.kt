package com.faltenreich.diaguard.dashboard

import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.dashboard.hba1c.DashboardHbA1cState
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
    fun `shows empty content if no data is available`() = runTest {
        // FIXME: Does not complete
        val state = viewModel.state.first()

        assertNull(state.latest)

        assertEquals(expected = 0, actual = state.today.totalCount)
        assertEquals(expected = 0, actual = state.today.hypoCount)
        assertEquals(expected = 0, actual = state.today.hyperCount)

        assertNull(state.average.day)
        assertNull(state.average.week)
        assertNull(state.average.month)

        assertTrue(state.hbA1c is DashboardHbA1cState.Unknown)

        assertTrue(state.trend.days.isEmpty())
    }

    @Test
    fun `shows latest blood sugar if data is available`() = runTest {
        importSeed()
        storeValue(value = 120.0, propertyKey = DatabaseKey.MeasurementProperty.BLOOD_SUGAR)

        val state = viewModel.state.first()

        assertNotNull(state.latest)
    }

    @Test
    fun `shows today's counts if data is available`() = runTest {
        importSeed()
        storeValue(value = 50.0, propertyKey = DatabaseKey.MeasurementProperty.BLOOD_SUGAR)
        storeValue(value = 120.0, propertyKey = DatabaseKey.MeasurementProperty.BLOOD_SUGAR)
        storeValue(value = 190.0, propertyKey = DatabaseKey.MeasurementProperty.BLOOD_SUGAR)

        val state = viewModel.state.first()

        assertEquals(expected = 3, actual = state.today.totalCount)
        assertEquals(expected = 1, actual = state.today.hypoCount)
        assertEquals(expected = 1, actual = state.today.hyperCount)
    }

    @Test
    fun `shows average values if data is available`() = runTest {
        importSeed()
        storeValue(value = 120.0, propertyKey = DatabaseKey.MeasurementProperty.BLOOD_SUGAR)

        val state = viewModel.state.first()

        assertNotNull(state.average.day)
        assertNotNull(state.average.week)
        assertNotNull(state.average.month)
    }

    @Test
    fun `shows latest HbA1c if data is available`() = runTest {
        importSeed()
        storeValue(value = 6.0, propertyKey = DatabaseKey.MeasurementProperty.HBA1C)

        val state = viewModel.state.first()
        assertTrue(state.hbA1c is DashboardHbA1cState.Latest)
    }

    @Test
    fun `shows estimated HbA1c if no values for HbA1c but Blood Sugar have been entered`() = runTest {
        importSeed()
        storeValue(value = 120.0, propertyKey = DatabaseKey.MeasurementProperty.BLOOD_SUGAR)

        val state = viewModel.state.first()
        assertTrue(state.hbA1c is DashboardHbA1cState.Estimated)
    }
}