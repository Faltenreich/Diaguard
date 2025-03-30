package com.faltenreich.diaguard.backup.seed

import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.tag.Tag

class SeedRepository(private val dao: SeedDao) {

    fun getMeasurementCategories(): List<MeasurementCategory.Seed> {
        return dao.getMeasurementCategories()
    }

    fun getFood(): List<Food.Seed> {
        return dao.getFood()
    }

    fun getTags(): List<Tag.Seed> {
        return dao.getTags()
    }
}