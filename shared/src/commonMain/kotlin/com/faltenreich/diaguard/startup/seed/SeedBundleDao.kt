package com.faltenreich.diaguard.startup.seed

import com.faltenreich.diaguard.startup.seed.query.food.FoodSeedQueries
import com.faltenreich.diaguard.startup.seed.query.measurement.MeasurementCategorySeedQueries
import com.faltenreich.diaguard.startup.seed.query.measurement.MeasurementUnitSeedQueries
import com.faltenreich.diaguard.startup.seed.query.tag.TagSeedQueries
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.tag.Tag

class SeedBundleDao(
    private val unitQueries: MeasurementUnitSeedQueries,
    private val categoryQueries: MeasurementCategorySeedQueries,
    private val foodQueries: FoodSeedQueries,
    private val tagQueries: TagSeedQueries,
) : SeedDao {

    override fun getUnits(): List<MeasurementUnit.Seed> {
        return unitQueries.getAll()
    }

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