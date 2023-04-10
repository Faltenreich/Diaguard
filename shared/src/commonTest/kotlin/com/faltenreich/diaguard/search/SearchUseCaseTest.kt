package com.faltenreich.diaguard.search

import app.cash.turbine.test
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.test.runTest
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import kotlin.test.*

class SearchUseCaseTest {

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
    fun `searches words`() = runTest(inject()) {
        val useCase = inject<SearchUseCase>()
        useCase("Query").test {
            assertTrue(awaitItem().isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }
}