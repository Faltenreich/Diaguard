@file:OptIn(ExperimentalCoroutinesApi::class)

package com.faltenreich.rhyme.search

import app.cash.turbine.test
import com.faltenreich.rhyme.mainModule
import com.faltenreich.rhyme.shared.di.inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runTest
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import kotlin.test.*

class SearchUseCaseTest {

    @BeforeTest
    fun setup() {
        startKoin {
            modules(mainModule())
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