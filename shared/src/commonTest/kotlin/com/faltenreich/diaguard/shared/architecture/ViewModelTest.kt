package com.faltenreich.diaguard.shared.architecture

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNull

class ViewModelTest {

    private lateinit var viewModel: SampleViewModel

    @BeforeTest
    fun setup() {
        viewModel = SampleViewModel()
    }

    @Test
    fun `state is initially null`() = runTest {
        assertNull(viewModel.state.firstOrNull())
    }

    private class SampleViewModel : ViewModel<Unit, Unit, Unit>(dispatcher = StandardTestDispatcher()) {
        override val state: Flow<Unit> = flowOf()
    }
}