package com.faltenreich.diaguard.export

import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.export.pdf.PdfLayout
import kotlinx.coroutines.test.runTest
import org.koin.test.inject
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ExportFormViewModelTest : TestSuite {

    private val viewModel: ExportFormViewModel by inject()
    private val dateTimeFactory: DateTimeFactory by inject()

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

    @Test
    fun `launch with date range of one week`() = runTest {
        val today = dateTimeFactory.today()
        assertEquals(
            expected = today.minus(1, DateUnit.WEEK) .. today,
            actual = viewModel.dateRange,
        )
    }

    @Test
    fun `launch with pdf layout of table`() = runTest {
        assertEquals(
            expected = PdfLayout.TABLE,
            actual = viewModel.pdfLayout,
        )
    }

    @Test
    fun `launch with including calendar week`() = runTest {
        assertEquals(
            expected = true,
            actual = viewModel.includeCalendarWeek,
        )
    }

    @Test
    fun `launch with including date of export`() = runTest {
        assertEquals(
            expected = true,
            actual = viewModel.includeDateOfExport,
        )
    }

    @Test
    fun `launch with including page number`() = runTest {
        assertEquals(
            expected = true,
            actual = viewModel.includePageNumber,
        )
    }

    @Test
    fun `launch with including notes`() = runTest {
        assertEquals(
            expected = true,
            actual = viewModel.includeNotes,
        )
    }

    @Test
    fun `launch with including tags`() = runTest {
        assertEquals(
            expected = true,
            actual = viewModel.includeTags,
        )
    }

    @Test
    fun `launch with including days without entries`() = runTest {
        assertEquals(
            expected = true,
            actual = viewModel.includeDaysWithoutEntries,
        )
    }
}