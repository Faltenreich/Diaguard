package com.faltenreich.diaguard.dashboard

import app.cash.turbine.test
import com.faltenreich.diaguard.DependencyInjection
import com.faltenreich.diaguard.appModules
import com.faltenreich.diaguard.backup.seed.SeedImport
import com.faltenreich.diaguard.testModules
import kotlinx.coroutines.test.runTest
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class DashboardViewModelTest : KoinTest {

    private val seedImport: SeedImport by inject()
    private val viewModel: DashboardViewModel by inject()

    @BeforeTest
    fun setup() {
        DependencyInjection.setup(modules = appModules() + testModules())
    }

    @Test
    fun `state is blank if no data available`() = runTest {
        viewModel.state.test {
            assertEquals(awaitItem(), DashboardViewState())
            awaitComplete()
        }
    }

    @Test
    fun `state contains blood sugar if available`() = runTest {
        seedImport.import()
        viewModel.state.test {
            assertEquals(awaitItem(), DashboardViewState())
            awaitComplete()
        }
    }
}