package com.faltenreich.diaguard.measurement.category.list

import com.faltenreich.diaguard.DependencyInjection
import com.faltenreich.diaguard.appModules
import com.faltenreich.diaguard.backup.ImportUseCase
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.testModules
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CreateMeasurementCategoryUseCaseTest : KoinTest {

    private val import: ImportUseCase by inject()

    private val createCategory: CreateMeasurementCategoryUseCase by inject()

    @BeforeTest
    fun setup() {
        DependencyInjection.setup(modules = appModules() + testModules())
        import()
    }

    @Test
    fun `creates and returns MeasurementCategory`() {
        val input = MeasurementCategory.User(
            name = "name",
            icon = "icon",
            sortIndex = 0,
            isActive = true,
        )

        val category = createCategory(input)

        assertEquals(input.name, category.name)
        assertEquals(input.icon, category.icon)
        assertEquals(input.sortIndex, category.sortIndex)
        assertEquals(input.isActive, category.isActive)
    }
}