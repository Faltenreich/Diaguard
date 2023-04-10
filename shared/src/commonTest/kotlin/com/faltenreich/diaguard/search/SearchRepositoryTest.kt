@file:OptIn(ExperimentalCoroutinesApi::class)

package com.faltenreich.diaguard.search

import app.cash.turbine.test
import com.faltenreich.diaguard.language.Language
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import kotlin.test.*

class SearchRepositoryTest {

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
    fun `search returns result`() = runTest {
        val repository = inject<SearchRepository>()
        repository.search("", Language.ENGLISH).test {
            assertNotNull(awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}