package com.faltenreich.diaguard.main

import com.faltenreich.diaguard.datetime.kotlinx.KotlinxDateTimeFactory
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryFakeDao
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class HasDataUseCaseTest {

    @Test
    fun `returns true if there are categories`() = runTest {
        val useCase = HasDataUseCase(
            categoryRepository = MeasurementCategoryRepository(
                dao = object : MeasurementCategoryFakeDao() {
                    override fun countAll(): Flow<Long> {
                        return flowOf(1L)
                    }
                },
                dateTimeFactory = KotlinxDateTimeFactory(),
            )
        )
        val result = useCase().first()
        assertTrue(result)
    }

    @Test
    fun `returns false if there are no categories`() = runTest {
        val useCase = HasDataUseCase(
            categoryRepository = MeasurementCategoryRepository(
                dao = object : MeasurementCategoryFakeDao() {
                    override fun countAll(): Flow<Long> {
                        return flowOf(0L)
                    }
                },
                dateTimeFactory = KotlinxDateTimeFactory(),
            )
        )
        val result = useCase().first()
        assertFalse(result)
    }
}