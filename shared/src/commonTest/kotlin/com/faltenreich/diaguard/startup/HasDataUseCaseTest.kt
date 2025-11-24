package com.faltenreich.diaguard.startup

import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.startup.seed.ImportSeedUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class HasDataUseCaseTest : TestSuite() {

    private val importSeed: ImportSeedUseCase by inject()
    private val hasData: HasDataUseCase by inject()

    @Test
    fun `returns true if there are categories`() = runTest {
        importSeed()

        val result = hasData().first()
        assertTrue(result)
    }

    @Test
    fun `returns false if there are no categories`() = runTest {
        val result = hasData().first()
        assertFalse(result)
    }
}