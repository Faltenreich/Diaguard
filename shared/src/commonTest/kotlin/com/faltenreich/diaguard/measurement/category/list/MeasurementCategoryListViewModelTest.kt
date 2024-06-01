package com.faltenreich.diaguard.measurement.category.list

import com.faltenreich.diaguard.DependencyInjection
import com.faltenreich.diaguard.appModules
import com.faltenreich.diaguard.backup.ImportUseCase
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.testModules
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.BeforeTest
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MeasurementCategoryListViewModelTest : KoinTest {

    private val import: ImportUseCase by inject()

    private val viewModel: MeasurementCategoryListViewModel by inject()

    @BeforeTest
    fun setup() {
        DependencyInjection.setup(modules = appModules() + testModules())
        import()
    }

    @Test
    fun `state contains categories`() = runTest {
        val categories = viewModel.state.first().categories
        assertTrue(categories.isNotEmpty())
    }

    @Test
    fun `state contains categories for every database key`() = runTest {
        val categories = viewModel.state.first().categories
        DatabaseKey.MeasurementCategory.entries.forEach { key ->
            assertTrue(categories.any { it.key == key })
        }
    }

    @Test
    @Ignore
    fun `decrements sort index of category`() = runTest{
        val firstCategory = viewModel.state.first().categories.first()
        viewModel.dispatchIntent(MeasurementCategoryListIntent.DecrementSortIndex(firstCategory))
        val update = viewModel.state.first().categories
        assertEquals(update[1], firstCategory)
    }
}