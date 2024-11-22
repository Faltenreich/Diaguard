package com.faltenreich.diaguard.backup.legacy

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.tag.EntryTag
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.eaten.FoodEaten
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.tag.Tag

expect class LegacySqliteDao : LegacyDao {

    override suspend fun getPreferences(): Map<String, String>

    override suspend fun getEntries(): List<Entry.Legacy>

    override suspend fun getMeasurementValues(): List<MeasurementValue.Legacy>

    override suspend fun getFood(): List<Food.Legacy>

    override suspend fun getFoodEaten(): List<FoodEaten.Legacy>

    override suspend fun getTags(): List<Tag.Legacy>

    override suspend fun getEntryTags(): List<EntryTag.Legacy>
}