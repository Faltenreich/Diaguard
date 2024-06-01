package com.faltenreich.diaguard.shared.architecture

import com.faltenreich.diaguard.DependencyInjection
import com.faltenreich.diaguard.appModules
import com.faltenreich.diaguard.testModules
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

class ViewModelTest : KoinTest {

    private val dispatcher: CoroutineDispatcher by inject()
    private lateinit var viewModel: TestViewModel

    @BeforeTest
    fun setup() {
        DependencyInjection.setup(modules = appModules() + testModules())
        viewModel = TestViewModel()
        Dispatchers.setMain(dispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
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