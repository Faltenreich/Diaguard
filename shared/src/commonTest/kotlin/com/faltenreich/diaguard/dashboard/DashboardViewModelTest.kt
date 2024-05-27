package com.faltenreich.diaguard.dashboard

import app.cash.turbine.test
import com.faltenreich.diaguard.DependencyInjection
import com.faltenreich.diaguard.appModules
import com.faltenreich.diaguard.testModules
import kotlinx.coroutines.test.runTest
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DashboardViewModelTest : KoinTest {

    private val viewModel: DashboardViewModel by inject()

    @BeforeTest
    fun setup() {
        DependencyInjection.setup(modules = appModules() + testModules())
    }

    @Test
    fun `state is initially null`() = runTest {
        viewModel.stateInScope.test {
            assertNull(awaitItem())
            assertTrue(awaitItem() is DashboardViewState.Revisit)
        }
    }
}