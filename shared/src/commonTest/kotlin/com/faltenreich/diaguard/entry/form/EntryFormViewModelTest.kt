package com.faltenreich.diaguard.entry.form

import app.cash.turbine.test
import com.faltenreich.diaguard.TestSuite
import kotlinx.coroutines.test.runTest
import org.koin.core.parameter.parametersOf
import org.koin.test.inject
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class EntryFormViewModelTest : TestSuite {

    private val viewModel: EntryFormViewModel by inject(
        parameters = { parametersOf(null, null, null) },
    )

    @BeforeTest
    override fun beforeTest() {
        super.beforeTest()
        importSeed()
    }

    @Test
    fun `suggests tags`() = runTest {
        viewModel.state.test {
            val state = awaitItem()
            assertTrue(state.tags.isNotEmpty())
        }
    }
}