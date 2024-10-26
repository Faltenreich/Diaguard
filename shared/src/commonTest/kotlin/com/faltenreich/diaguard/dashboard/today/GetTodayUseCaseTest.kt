package com.faltenreich.diaguard.dashboard.today

import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.backup.ImportUseCase
import com.faltenreich.diaguard.dashboard.DashboardState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.koin.test.inject
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetTodayUseCaseTest : TestSuite {

    private val getToday by inject<GetTodayUseCase>()
    private val import by inject<ImportUseCase>()

    @BeforeTest
    override fun beforeTest() {
        super.beforeTest()
        import()
    }

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
}