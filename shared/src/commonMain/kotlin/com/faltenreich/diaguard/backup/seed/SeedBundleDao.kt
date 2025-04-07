package com.faltenreich.diaguard.backup.seed

import com.faltenreich.diaguard.backup.seed.query.food.FoodSeedQueries
import com.faltenreich.diaguard.backup.seed.query.measurement.MeasurementCategorySeedQueries
import com.faltenreich.diaguard.backup.seed.query.tag.TagSeedQueries
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.tag.Tag

class SeedBundleDao(
    private val categoryQueries: MeasurementCategorySeedQueries,
    private val foodQueries: FoodSeedQueries,
    private val tagQueries: TagSeedQueries,
) : SeedDao {

    override fun getCategories(): List<MeasurementCategory.Seed> {
        return categoryQueries.getAll()
    }

    override fun getFood(): List<Food.Seed> {
        return foodQueries.getAll()
    }

    override fun getTags(): List<Tag.Seed> {
        return tagQueries.getAll()
    }
}