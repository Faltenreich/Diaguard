package com.faltenreich.diaguard.shared.architecture

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

class ViewModelTest {

    private lateinit var viewModel: SampleViewModel

    @BeforeTest
    fun setup() {
        viewModel = SampleViewModel()
    }

    @Test
    fun `handles intent`() = runTest {
        viewModel.handleIntent(2)
        assertEquals(2, viewModel.state.value)
    }

    @Test
    @Ignore
    fun `dispatches intent`() = runTest {
        viewModel.dispatchIntent(2)
        assertEquals(2, viewModel.state.value)
    }

    @Test
    @Ignore
    fun `collects events`() = runTest {
        viewModel.collectEvents { event ->
            assertEquals(2, event)
        }
        viewModel.postEvent(2)
    }

    private class SampleViewModel : ViewModel<Int, Int, Int>(dispatcher = StandardTestDispatcher()) {

        override val state = MutableStateFlow(0)

        override suspend fun handleIntent(intent: Int) {
            state.value += intent
        }
    }
}