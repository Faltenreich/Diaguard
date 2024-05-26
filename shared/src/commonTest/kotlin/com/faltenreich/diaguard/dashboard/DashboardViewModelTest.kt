package com.faltenreich.diaguard.dashboard

import com.faltenreich.diaguard.DependencyInjection
import com.faltenreich.diaguard.appModules
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.testModules
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.test.KoinTest
import kotlin.test.Test
import kotlin.test.assertEquals

class DashboardViewModelTest : KoinTest {

    @Test
    fun `state is initially null`() = runBlocking {
        // TODO: Implement operations
        val modules = appModules() + testModules()
        DependencyInjection.setup(modules = modules)
        val viewModel: DashboardViewModel = inject()
        val state = viewModel.stateInScope.first()
        assertEquals(null, state)
    }
}