package com.faltenreich.diaguard.measurement.category

import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.measurement.category.usecase.StoreMeasurementCategoryUseCase
import org.koin.test.inject
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class StoreMeasurementCategoryUseCaseTest : TestSuite {

    private val storeCategory: StoreMeasurementCategoryUseCase by inject()

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

        val category = storeCategory(input)

        assertEquals(input.name, category.name)
        assertEquals(input.icon, category.icon)
        assertEquals(input.sortIndex, category.sortIndex)
        assertEquals(input.isActive, category.isActive)
    }
}