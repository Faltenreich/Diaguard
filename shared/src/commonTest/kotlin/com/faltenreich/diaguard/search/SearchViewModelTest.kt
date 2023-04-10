package com.faltenreich.diaguard.search

import app.cash.turbine.test
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.test.runTest
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import kotlin.test.*

class SearchViewModelTest: KoinTest {

    @BeforeTest
    fun setup() {
        startKoin {
            modules(com.faltenreich.diaguard.mainModule())
        }
    }

    @AfterTest
    fun clean() {
        stopKoin()
    }

    @Test
    fun `opens in idle state`() {
        val viewModel = inject<SearchViewModel>()
        assertEquals(SearchState.Idle, viewModel.uiState.value)
    }

    @Test
    fun `reaches idle state on blank query`() = runTest {
        val viewModel = inject<SearchViewModel>()
        viewModel.uiState.test {
            viewModel.onQueryChanged("")
            assertTrue(awaitItem() is SearchState.Idle)
            // TODO: Verify last item
        }
    }

    @Test
    fun `reaches result state on non-blank query`() = runTest {
        val viewModel = inject<SearchViewModel>()
        viewModel.uiState.test {
            viewModel.onQueryChanged("Query")
            assertTrue(awaitItem() is SearchState.Idle)
            assertTrue(awaitItem() is SearchState.Loading)
            // FIXME: No value produced in 1s
            assertTrue(awaitItem() is SearchState.Result)
        }
    }
}