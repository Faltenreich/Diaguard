package com.faltenreich.diaguard.export

import app.cash.turbine.test
import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.export.form.ExportFormMeasurementCategory
import com.faltenreich.diaguard.export.form.ExportFormViewModel
import com.faltenreich.diaguard.export.pdf.PdfLayout
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.koin.test.inject
import kotlin.test.BeforeTest
import kotlin.test.Ignore
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
    @Ignore
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
                expected = exportCategories,
                actual = awaitItem().content.categories,
            )
        }
    }
}