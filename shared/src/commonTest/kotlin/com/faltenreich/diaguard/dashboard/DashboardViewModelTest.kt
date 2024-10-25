package com.faltenreich.diaguard.dashboard

import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.backup.ImportUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class DashboardViewModelTest : TestSuite {

    private val import: ImportUseCase by inject()

    private val viewModel: DashboardViewModel by inject()

    @Test
    fun `state contains null content if no data is available`() = runTest {
        assertEquals(
            DashboardState(
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
    fun `state contains content if seed data is available`() = runTest {
        import()

        val state = viewModel.state.first()

        assertNotNull(state.latestBloodSugar)
        assertNotNull(state.today)
        assertNotNull(state.average)
        assertNotNull(state.hbA1c)
        assertNull(state.trend)
    }
}