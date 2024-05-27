package com.faltenreich.diaguard.shared.architecture

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ViewModelTest {

    @Test
    fun `state is initially null`() = runTest {
        val viewModel = SampleViewModel()
        val state = viewModel.stateInScope.first()
        assertEquals(null, state)
    }

    private class SampleViewModel : ViewModel<Unit, Unit, Unit>(dispatcher = StandardTestDispatcher()) {
        override val state: Flow<Unit> = flowOf()
    }
}