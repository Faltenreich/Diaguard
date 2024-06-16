package com.faltenreich.diaguard.backup.seed

import com.faltenreich.diaguard.backup.seed.query.FoodSeedQueries
import com.faltenreich.diaguard.backup.seed.query.MeasurementCategorySeedQueries
import com.faltenreich.diaguard.backup.seed.query.TagSeedQueries
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.tag.Tag

class SeedRepository(
    private val measurementCategoryQueries: MeasurementCategorySeedQueries,
    private val foodQueries: FoodSeedQueries,
    private val tagQueries: TagSeedQueries,
) {

    fun getMeasurementCategories(): List<MeasurementCategory.Seed> {
        return measurementCategoryQueries()
    }

    fun getFood(): List<Food.Seed> {
        return foodQueries()
    }

    fun getTags(): List<Tag.Seed> {
        return tagQueries()
    }
}