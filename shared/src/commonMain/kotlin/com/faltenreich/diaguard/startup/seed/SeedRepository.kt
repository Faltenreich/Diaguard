package com.faltenreich.diaguard.startup.seed

import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.tag.Tag

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