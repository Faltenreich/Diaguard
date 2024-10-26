package com.faltenreich.diaguard.dashboard.today

import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.dashboard.DashboardState
import com.faltenreich.diaguard.shared.database.DatabaseKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertEquals

class GetTodayUseCaseTest : TestSuite {

    private val getToday by inject<GetTodayUseCase>()

    @Test
    fun `returns count of zero if no blood sugar is available today`() = runTest {
        assertEquals(
            expected = DashboardState.Today(
                totalCount = 0,
                hypoCount = 0,
                hyperCount = 0,
            ),
            actual = getToday().first(),
        )
    }

    @Test
    fun `returns count of non-zero if blood sugar is available today`() = runTest {
        importSeed()

        storeValue(value = 120.0, propertyKey = DatabaseKey.MeasurementProperty.BLOOD_SUGAR)
        storeValue(value = 200.0, propertyKey = DatabaseKey.MeasurementProperty.BLOOD_SUGAR)
        storeValue(value = 50.0, propertyKey = DatabaseKey.MeasurementProperty.BLOOD_SUGAR)

        assertEquals(
            expected = DashboardState.Today(
                totalCount = 3,
                hypoCount = 1,
                hyperCount = 1,
            ),
            actual = getToday().first(),
        )
    }
}