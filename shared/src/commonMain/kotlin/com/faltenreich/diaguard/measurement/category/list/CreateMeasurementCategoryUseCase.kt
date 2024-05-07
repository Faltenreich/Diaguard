package com.faltenreich.diaguard.measurement.category.list

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryRepository

class CreateMeasurementCategoryUseCase(
    private val measurementCategoryRepository: MeasurementCategoryRepository,
    private val dateTimeFactory: DateTimeFactory,
) {

    operator fun invoke(
        name: String,
        icon: String?,
    ): MeasurementCategory {
        val now = dateTimeFactory.now()
        val sortIndex = measurementCategoryRepository.getAll().maxBy { it.sortIndex }.sortIndex + 1
        val id = measurementCategoryRepository.create(
            createdAt = now,
            updatedAt = now,
            name = name,
            key = null,
            icon = icon,
            sortIndex = sortIndex,
        )
        return MeasurementCategory(
            id = id,
            createdAt = now,
            updatedAt = now,
            name = name,
            key = null,
            icon = icon,
            sortIndex = sortIndex,
        )
    }
}