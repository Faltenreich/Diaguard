package com.faltenreich.diaguard.measurement.category.usecase

import com.faltenreich.diaguard.data.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.measurementModule
import com.faltenreich.diaguard.startup.seed.ImportSeedUseCase
import com.faltenreich.diaguard.startup.startupModule
import com.faltenreich.diaguard.test.TestSuite
import org.koin.test.inject
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class StoreMeasurementCategoryUseCaseTest : TestSuite(measurementModule() + startupModule()) {

    private val importSeed: ImportSeedUseCase by inject()
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