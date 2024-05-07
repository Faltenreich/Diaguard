package com.faltenreich.diaguard.measurement.category.list

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryRepository
import com.faltenreich.diaguard.shared.database.DatabaseKey

class CreateMeasurementCategoryUseCase(
    private val measurementCategoryRepository: MeasurementCategoryRepository,
    private val dateTimeFactory: DateTimeFactory,
) {

    operator fun invoke(
        name: String,
        key: DatabaseKey.MeasurementCategory?,
        icon: String?,
        sortIndex: Long,
    ): MeasurementCategory {
        val now = dateTimeFactory.now()
        val id = measurementCategoryRepository.create(
            createdAt = now,
            updatedAt = now,
            name = name,
            key = key?.key,
            icon = icon,
            sortIndex = sortIndex,
        )
        return MeasurementCategory(
            id = id,
            createdAt = now,
            updatedAt = now,
            name = name,
            key = key,
            icon = icon,
            sortIndex = sortIndex,
        )
    }

    operator fun invoke(): MeasurementCategory {
        val maxSortIndex = measurementCategoryRepository.getAll().maxBy { it.sortIndex }.sortIndex
        return invoke(
            name = "",
            key = null,
            icon = null,
            sortIndex = maxSortIndex + 1,
        )
    }
}