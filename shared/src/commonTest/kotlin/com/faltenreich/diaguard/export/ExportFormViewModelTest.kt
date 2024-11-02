package com.faltenreich.diaguard.export

import com.faltenreich.diaguard.TestSuite
import kotlinx.coroutines.test.runTest
import org.koin.test.inject
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ExportFormViewModelTest : TestSuite {

    private val viewModel: ExportFormViewModel by inject()

    @BeforeTest
    override fun beforeTest() {
        super.beforeTest()
        importSeed()
    }

    @Test
    fun `launch with export type of pdf`() = runTest {
        assertEquals(
            expected = ExportType.PDF,
            actual = viewModel.exportType,
        )
    }
}