package com.faltenreich.diaguard.backup.seed

import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.tag.Tag

interface SeedDao {

    fun getMeasurementCategories(): List<MeasurementCategory.Seed>

    fun getFood(): List<Food.Seed>

    fun getTags(): List<Tag.Seed>
}