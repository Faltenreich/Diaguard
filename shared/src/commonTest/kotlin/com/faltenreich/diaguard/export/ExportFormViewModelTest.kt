package com.faltenreich.diaguard.export

import app.cash.turbine.test
import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.export.form.ExportFormIntent
import com.faltenreich.diaguard.export.form.ExportFormMeasurementCategory
import com.faltenreich.diaguard.export.form.ExportFormViewModel
import com.faltenreich.diaguard.export.pdf.PdfLayout
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.koin.test.inject
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class ExportFormViewModelTest : TestSuite {

    private val viewModel: ExportFormViewModel by inject()
    private val dateTimeFactory: DateTimeFactory by inject()
    private val categoryRepository: MeasurementCategoryRepository by inject()

    @BeforeTest
    override fun beforeTest() {
        super.beforeTest()
        importSeed()
    }

    @Test
    fun `launch with export type of pdf`() = runTest {
        viewModel.state.test {
            assertEquals(
                expected = ExportType.PDF,
                actual = awaitItem().type.selection,
            )
        }
    }

    @Test
    fun `launch with date range of one week`() = runTest {
        viewModel.state.test {
            val today = dateTimeFactory.today()
            assertEquals(
                expected = today.minus(1, DateUnit.WEEK) .. today,
                actual = awaitItem().date.dateRange,
            )
        }
    }

    @Test
    fun `launch with pdf layout of table`() = runTest {
        viewModel.state.test {
            assertEquals(
                expected = PdfLayout.TABLE,
                actual = awaitItem().layout.selection,
            )
        }
    }

    @Test
    fun `launch with including calendar week`() = runTest {
        viewModel.state.test {
            assertEquals(
                expected = true,
                actual = awaitItem().date.includeCalendarWeek,
            )
        }
    }

    @Test
    fun `launch with including date of export`() = runTest {
        viewModel.state.test {
            assertEquals(
                expected = true,
                actual = awaitItem().date.includeDateOfExport,
            )
        }
    }

    @Test
    fun `launch with including page number`() = runTest {
        viewModel.state.test {
            assertEquals(
                expected = true,
                actual = awaitItem().layout.includePageNumber,
            )
        }
    }

    @Test
    fun `launch with including notes`() = runTest {
        viewModel.state.test {
            assertEquals(
                expected = true,
                actual = awaitItem().content.includeNotes,
            )
        }
    }

    @Test
    fun `launch with including tags`() = runTest {
        viewModel.state.test {
            assertEquals(
                expected = true,
                actual = awaitItem().content.includeTags,
            )
        }
    }

    @Test
    fun `launch with including days without entries`() = runTest {
        viewModel.state.test {
            assertEquals(
                expected = true,
                actual = awaitItem().layout.includeDaysWithoutEntries,
            )
        }
    }

    @Test
    fun `launch with all categories enabled`() = runTest {
        val categories = categoryRepository.observeAll().first()
        val exportCategories = categories.map { category ->
            ExportFormMeasurementCategory(
                category = category,
                isExported = true,
                isMerged = false,
            )
        }
        viewModel.state.test {
            assertContentEquals(
                expected = emptyList(),
                actual = awaitItem().content.categories,
            )
            assertContentEquals(
                expected = exportCategories,
                actual = awaitItem().content.categories,
            )
        }
    }

    @Test
    fun `update state when intending to set date range`() = runTest {
        val dateRangeEnd = dateTimeFactory.date(year = 1970, monthNumber = 1, dayOfMonth = 1)
        val dateRangeStart = dateRangeEnd.minus(1, DateUnit.WEEK)
        val dateRange = dateRangeStart .. dateRangeEnd

        viewModel.handleIntent(ExportFormIntent.SetDateRange(dateRange))

        viewModel.state.test {
            assertEquals(
                expected = dateRange,
                awaitItem().date.dateRange,
            )
        }
    }

    @Test
    fun `update state when intending to select type`() = runTest {
        val type = ExportType.CSV
        viewModel.handleIntent(ExportFormIntent.SelectType(type))

        viewModel.state.test {
            assertEquals(
                expected = type,
                actual = awaitItem().type.selection,
            )
        }
    }

    @Test
    fun `update state when intending to select layout`() = runTest {
        val layout = PdfLayout.LOG
        viewModel.handleIntent(ExportFormIntent.SelectLayout(layout))

        viewModel.state.test {
            assertEquals(
                expected = layout,
                actual = awaitItem().layout.selection,
            )
        }
    }

    @Test
    fun `update state when intending to set includeCalendarWeek`() = runTest {
        val includeCalendarWeek = false
        viewModel.handleIntent(ExportFormIntent.SetIncludeCalendarWeek(includeCalendarWeek))

        viewModel.state.test {
            assertEquals(
                expected = includeCalendarWeek,
                actual = awaitItem().date.includeCalendarWeek,
            )
        }
    }

    @Test
    fun `update state when intending to set includeDateOfExport`() = runTest {
        val includeDateOfExport = false
        viewModel.handleIntent(ExportFormIntent.SetIncludeDateOfExport(includeDateOfExport))

        viewModel.state.test {
            assertEquals(
                expected = includeDateOfExport,
                actual = awaitItem().date.includeDateOfExport,
            )
        }
    }

    @Test
    fun `update state when intending to set includePageNumber`() = runTest {
        val includePageNumber = false
        viewModel.handleIntent(ExportFormIntent.SetIncludePageNumber(includePageNumber))

        viewModel.state.test {
            assertEquals(
                expected = includePageNumber,
                actual = awaitItem().layout.includePageNumber,
            )
        }
    }

    @Test
    fun `update state when intending to set includeNotes`() = runTest {
        val includeNotes = false
        viewModel.handleIntent(ExportFormIntent.SetIncludeNotes(includeNotes))

        viewModel.state.test {
            assertEquals(
                expected = includeNotes,
                actual = awaitItem().content.includeNotes,
            )
        }
    }

    @Test
    fun `update state when intending to set includeTags`() = runTest {
        val includeTags = false
        viewModel.handleIntent(ExportFormIntent.SetIncludeTags(includeTags))

        viewModel.state.test {
            assertEquals(
                expected = includeTags,
                actual = awaitItem().content.includeTags,
            )
        }
    }

    @Test
    fun `update state when intending to set includeDaysWithoutEntries`() = runTest {
        val includeDaysWithoutEntries = false
        viewModel.handleIntent(ExportFormIntent.SetIncludeDaysWithoutEntries(includeDaysWithoutEntries))

        viewModel.state.test {
            assertEquals(
                expected = includeDaysWithoutEntries,
                actual = awaitItem().layout.includeDaysWithoutEntries,
            )
        }
    }
}