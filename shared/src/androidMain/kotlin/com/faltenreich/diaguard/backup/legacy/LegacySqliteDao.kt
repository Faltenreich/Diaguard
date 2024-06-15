package com.faltenreich.diaguard.backup.legacy

import com.faltenreich.diaguard.backup.legacy.dao.LegacyEntrySqliteDao
import com.faltenreich.diaguard.backup.legacy.dao.LegacyEntryTagSqliteDao
import com.faltenreich.diaguard.backup.legacy.dao.LegacyFoodEatenSqliteDao
import com.faltenreich.diaguard.backup.legacy.dao.LegacyFoodSqliteDao
import com.faltenreich.diaguard.backup.legacy.dao.LegacyMeasurementValueSqliteDao
import com.faltenreich.diaguard.backup.legacy.dao.LegacyTagSqliteDao
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.tag.EntryTag
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.eaten.FoodEaten
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.tag.Tag

actual class LegacySqliteDao(
    private val entryDao: LegacyEntrySqliteDao,
    private val measurementValueDao: LegacyMeasurementValueSqliteDao,
    private val foodDao: LegacyFoodSqliteDao,
    private val foodEatenDao: LegacyFoodEatenSqliteDao,
    private val tagDao: LegacyTagSqliteDao,
    private val entryTagDao: LegacyEntryTagSqliteDao,
) : LegacyDao {

    override fun getEntries(): List<Entry.Legacy> {
        return entryDao.getEntries()
    }

    override fun getMeasurementValues(): List<MeasurementValue.Legacy> {
        return measurementValueDao.getMeasurementValues()
    }

    override fun getFood(): List<Food.Legacy> {
        return foodDao.getFood()
    }

    override fun getFoodEaten(): List<FoodEaten.Legacy> {
        return foodEatenDao.getFoodEaten()
    }

    override fun getTags(): List<Tag.Legacy> {
        return tagDao.getTags()
    }

    override fun getEntryTags(): List<EntryTag.Legacy> {
        return entryTagDao.getEntryTags()
    }
}