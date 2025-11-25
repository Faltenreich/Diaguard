package com.faltenreich.diaguard.data.seed

import com.faltenreich.diaguard.data.seed.query.food.FoodSeedQueries
import com.faltenreich.diaguard.data.seed.query.measurement.MeasurementCategorySeedQueries
import com.faltenreich.diaguard.data.seed.query.measurement.MeasurementUnitSeedQueries
import com.faltenreich.diaguard.data.seed.query.tag.TagSeedQueries
import com.faltenreich.diaguard.data.food.Food
import com.faltenreich.diaguard.data.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.data.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.data.tag.Tag

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