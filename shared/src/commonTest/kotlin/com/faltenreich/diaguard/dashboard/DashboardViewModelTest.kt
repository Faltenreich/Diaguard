package com.faltenreich.diaguard.dashboard

import com.faltenreich.diaguard.DependencyInjection
import com.faltenreich.diaguard.appModules
import com.faltenreich.diaguard.testModules
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertEquals

class DashboardViewModelTest : KoinTest {

    private val viewModel: DashboardViewModel by inject()

    init {
        DependencyInjection.setup(modules = appModules() + testModules())
    }

    @Test
    fun `state is initially null`() = runBlocking {
        val state = viewModel.stateInScope.first()
        assertEquals(null, state)
    }
}