package com.faltenreich.diaguard.measurement.category.list

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryRepository

class CreateMeasurementCategoryUseCase(
    private val repository: MeasurementCategoryRepository,
    private val dateTimeFactory: DateTimeFactory,
) {

    operator fun invoke(
        name: String,
        icon: String?,
        sortIndex: Long,
    ): MeasurementCategory {
        val createdAt = dateTimeFactory.now()
        val id = repository.create(
            createdAt = createdAt,
            updatedAt = createdAt,
            name = name,
            key = null,
            icon = icon,
            sortIndex = sortIndex,
            isActive = true,
        )
        return MeasurementCategory(
            id = id,
            createdAt = createdAt,
            updatedAt = createdAt,
            name = name,
            key = null,
            icon = icon,
            sortIndex = sortIndex,
            isActive = true,
        )
    }
}