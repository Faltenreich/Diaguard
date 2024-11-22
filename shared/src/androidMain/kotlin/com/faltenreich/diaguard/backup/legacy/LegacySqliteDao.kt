package com.faltenreich.diaguard.backup.legacy

import com.faltenreich.diaguard.backup.legacy.query.EntryLegacyQueries
import com.faltenreich.diaguard.backup.legacy.query.EntryTagLegacyQueries
import com.faltenreich.diaguard.backup.legacy.query.FoodEatenLegacyQueries
import com.faltenreich.diaguard.backup.legacy.query.FoodLegacyQueries
import com.faltenreich.diaguard.backup.legacy.query.MeasurementValueLegacyQueries
import com.faltenreich.diaguard.backup.legacy.query.TagLegacyQueries
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.tag.EntryTag
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.eaten.FoodEaten
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.shared.keyvalue.KeyValueStore
import com.faltenreich.diaguard.shared.keyvalue.read
import com.faltenreich.diaguard.tag.Tag
import kotlinx.coroutines.flow.first

actual class LegacySqliteDao(
    private val keyValueStore: KeyValueStore,
    private val entryQueries: EntryLegacyQueries,
    private val measurementValueQueries: MeasurementValueLegacyQueries,
    private val foodQueries: FoodLegacyQueries,
    private val foodEatenQueries: FoodEatenLegacyQueries,
    private val tagQueries: TagLegacyQueries,
    private val entryTagQueries: EntryTagLegacyQueries,
) : LegacyDao {

    actual override suspend fun getPreferences(): Map<String, String> {
        val theme = keyValueStore.read<String>("theme").first() ?: ""
        return mapOf("theme" to theme)
    }

    actual override suspend fun getEntries(): List<Entry.Legacy> {
        return entryQueries.getAll()
    }

    actual override suspend fun getMeasurementValues(): List<MeasurementValue.Legacy> {
        return measurementValueQueries.getAll()
    }

    actual override suspend fun getFood(): List<Food.Legacy> {
        return foodQueries.getAll()
    }

    actual override suspend fun getFoodEaten(): List<FoodEaten.Legacy> {
        return foodEatenQueries.getAll()
    }

    actual override suspend fun getTags(): List<Tag.Legacy> {
        return tagQueries.getAll()
    }

    actual override suspend fun getEntryTags(): List<EntryTag.Legacy> {
        return entryTagQueries.getAll()
    }
}