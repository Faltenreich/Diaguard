package com.faltenreich.diaguard.measurement.category.list

import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import org.koin.test.inject
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CreateMeasurementCategoryUseCaseTest : TestSuite {

    private val createCategory: CreateMeasurementCategoryUseCase by inject()

    @BeforeTest
    override fun beforeTest() {
        super.beforeTest()
        importSeed()
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