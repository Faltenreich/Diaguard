package com.faltenreich.diaguard.data.seed

import com.faltenreich.diaguard.data.food.Food
import com.faltenreich.diaguard.data.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.data.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.data.tag.Tag

class SeedRepository(private val dao: SeedDao) {

    fun getUnits(): List<MeasurementUnit.Seed> {
        return dao.getUnits()
    }

    fun getCategories(): List<MeasurementCategory.Seed> {
        return dao.getCategories()
    }

    fun getFood(): List<Food.Seed> {
        return dao.getFood()
    }

    fun getTags(): List<Tag.Seed> {
        return dao.getTags()
    }
}