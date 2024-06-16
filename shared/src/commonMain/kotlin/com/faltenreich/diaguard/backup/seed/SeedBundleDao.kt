package com.faltenreich.diaguard.backup.seed

import com.faltenreich.diaguard.backup.seed.query.FoodSeedQueries
import com.faltenreich.diaguard.backup.seed.query.MeasurementCategorySeedQueries
import com.faltenreich.diaguard.backup.seed.query.TagSeedQueries
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.tag.Tag

class SeedBundleDao(
    private val measurementCategoryQueries: MeasurementCategorySeedQueries,
    private val foodQueries: FoodSeedQueries,
    private val tagQueries: TagSeedQueries,
) : SeedDao {

    override fun getMeasurementCategories(): List<MeasurementCategory.Seed> {
        return measurementCategoryQueries.getAll()
    }

    override fun getFood(): List<Food.Seed> {
        return foodQueries.getAll()
    }

    override fun getTags(): List<Tag.Seed> {
        return tagQueries.getAll()
    }
}