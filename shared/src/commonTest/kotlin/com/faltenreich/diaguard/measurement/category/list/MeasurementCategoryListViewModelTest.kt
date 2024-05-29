package com.faltenreich.diaguard.measurement.category.list

import app.cash.turbine.test
import com.faltenreich.diaguard.DependencyInjection
import com.faltenreich.diaguard.appModules
import com.faltenreich.diaguard.backup.ImportUseCase
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.testModules
import kotlinx.coroutines.test.runTest
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.BeforeTest
import kotlin.test.Test
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
        viewModel.state.test {
            assertTrue(awaitItem().categories.isNotEmpty())
            awaitComplete()
        }
    }

    @Test
    fun `state contains categories for every database key`() = runTest {
        viewModel.state.test {
            val categories = awaitItem().categories
            DatabaseKey.MeasurementCategory.entries.forEach { key ->
                assertTrue(categories.any { it.key == key })
            }
            awaitComplete()
        }
    }
}