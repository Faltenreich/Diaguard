package com.faltenreich.diaguard.dashboard

import com.faltenreich.diaguard.DependencyInjection
import com.faltenreich.diaguard.appModules
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.test.KoinTest
import kotlin.test.Test
import kotlin.test.assertEquals

class DashboardViewModelTest : KoinTest {

    @Test
    fun `state is initially null`() = runBlocking {
        // FIXME: MissingAndroidContextException: Can't resolve Context instance
        DependencyInjection.setup(modules = appModules())
        val viewModel: DashboardViewModel = inject()
        val state = viewModel.stateInScope.first()
        assertEquals(null, state)
    }
}