@file:OptIn(ExperimentalCoroutinesApi::class)

package com.faltenreich.rhyme.search

import app.cash.turbine.test
import com.faltenreich.rhyme.language.Language
import com.faltenreich.rhyme.mainModule
import com.faltenreich.rhyme.shared.di.inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import kotlin.test.*

class SearchRepositoryTest {

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
    fun `search returns result`() = runTest {
        val repository = inject<SearchRepository>()
        repository.search("", Language.ENGLISH).test {
            assertNotNull(awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}