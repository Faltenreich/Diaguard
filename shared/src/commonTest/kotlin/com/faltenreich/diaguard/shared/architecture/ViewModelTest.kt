package com.faltenreich.diaguard.shared.architecture

import com.faltenreich.diaguard.DependencyInjection
import com.faltenreich.diaguard.appModules
import com.faltenreich.diaguard.testModules
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.koin.test.KoinTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ViewModelTest : KoinTest {

    @BeforeTest
    fun setup() {
        DependencyInjection.setup(modules = appModules() + testModules())
    }

    @Test
    fun `state is initially null`() = runTest {
        val viewModel = SampleViewModel()
        val state = viewModel.stateInScope.first()
        assertEquals(null, state)
    }

    private class SampleViewModel : ViewModel<Unit, Unit, Unit>() {
        override val state: Flow<Unit> = flowOf()
    }
}