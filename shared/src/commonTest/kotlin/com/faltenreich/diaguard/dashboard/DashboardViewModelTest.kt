package com.faltenreich.diaguard.dashboard

import app.cash.turbine.test
import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.dashboard.hba1c.DashboardHbA1cState
import com.faltenreich.diaguard.data.DatabaseKey
import com.faltenreich.diaguard.measurement.value.usecase.StoreMeasurementValueUseCase
import com.faltenreich.diaguard.startup.seed.ImportSeedUseCase
import kotlinx.coroutines.test.runTest
import org.koin.test.inject
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DashboardViewModelTest : TestSuite() {

    private val viewModel: DashboardViewModel by inject()
    private val importSeed: ImportSeedUseCase by inject()
    private val storeValue: StoreMeasurementValueUseCase by inject()

    // FIXME: Never completes
    @Ignore
    @Test
    fun `shows empty content if no data is available`() = runTest {
        viewModel.state.test {
            val state = awaitItem()

            assertNull(state.latest)

            assertEquals(expected = 0, actual = state.today.totalCount)
            assertEquals(expected = 0, actual = state.today.hypoCount)
            assertEquals(expected = 0, actual = state.today.hyperCount)

            assertNull(state.average.day)
            assertNull(state.average.week)
            assertNull(state.average.month)

            assertTrue(state.hbA1c is DashboardHbA1cState.Unknown)

            assertTrue(state.trend.intervals.isEmpty())
        }
    }

    @Test
    fun `shows latest blood sugar if data is available`() = runTest {
        importSeed()
        storeValue(value = 120.0, propertyKey = DatabaseKey.MeasurementProperty.BLOOD_SUGAR)

        viewModel.state.test {
            val state = awaitItem()

            assertNotNull(state.latest)
        }
    }

    @Test
    fun `shows today's counts if data is available`() = runTest {
        importSeed()
        storeValue(value = 50.0, propertyKey = DatabaseKey.MeasurementProperty.BLOOD_SUGAR)
        storeValue(value = 120.0, propertyKey = DatabaseKey.MeasurementProperty.BLOOD_SUGAR)
        storeValue(value = 190.0, propertyKey = DatabaseKey.MeasurementProperty.BLOOD_SUGAR)

        viewModel.state.test {
            val state = awaitItem()

            assertEquals(expected = 3, actual = state.today.totalCount)
            assertEquals(expected = 1, actual = state.today.hypoCount)
            assertEquals(expected = 1, actual = state.today.hyperCount)
        }
    }

    @Test
    fun `shows average values if data is available`() = runTest {
        importSeed()
        storeValue(value = 120.0, propertyKey = DatabaseKey.MeasurementProperty.BLOOD_SUGAR)

        viewModel.state.test {
            val state = awaitItem()

            assertNotNull(state.average.day)
            assertNotNull(state.average.week)
            assertNotNull(state.average.month)
        }
    }

    @Test
    fun `shows latest HbA1c if data is available`() = runTest {
        importSeed()
        storeValue(value = 6.0, propertyKey = DatabaseKey.MeasurementProperty.HBA1C)

        viewModel.state.test {
            val state = awaitItem()

            assertTrue(state.hbA1c is DashboardHbA1cState.Latest)
        }
    }

    @Test
    fun `shows estimated HbA1c if no values for HbA1c but Blood Sugar have been entered`() = runTest {
        importSeed()
        storeValue(value = 120.0, propertyKey = DatabaseKey.MeasurementProperty.BLOOD_SUGAR)

        viewModel.state.test {
            val state = awaitItem()

            assertTrue(state.hbA1c is DashboardHbA1cState.Estimated)
        }
    }
}