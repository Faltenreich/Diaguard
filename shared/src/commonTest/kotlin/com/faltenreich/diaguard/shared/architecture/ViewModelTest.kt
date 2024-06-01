package com.faltenreich.diaguard.shared.architecture

import com.faltenreich.diaguard.TestSuite
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

class ViewModelTest : TestSuite {

    private lateinit var viewModel: TestViewModel

    @BeforeTest
    override fun beforeTest() {
        super.beforeTest()
        viewModel = TestViewModel()
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
        advanceUntilIdle()
    }

    private class TestViewModel : ViewModel<Int, Int, Int>() {

        override val state = MutableStateFlow(0)

        override suspend fun handleIntent(intent: Int) {
            state.value += intent
        }
    }
}