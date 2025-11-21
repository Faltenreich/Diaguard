package com.faltenreich.diaguard.startup.seed

import com.faltenreich.diaguard.data.food.Food
import com.faltenreich.diaguard.data.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.data.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.data.tag.Tag

interface SeedDao {

    fun getUnits(): List<MeasurementUnit.Seed>

    fun getCategories(): List<MeasurementCategory.Seed>

    fun getFood(): List<Food.Seed>

    fun getTags(): List<Tag.Seed>
}