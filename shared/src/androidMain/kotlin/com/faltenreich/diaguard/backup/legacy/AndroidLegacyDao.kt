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

class AndroidLegacyDao(
    private val keyValueStore: KeyValueStore,
    private val entryQueries: EntryLegacyQueries,
    private val measurementValueQueries: MeasurementValueLegacyQueries,
    private val foodQueries: FoodLegacyQueries,
    private val foodEatenQueries: FoodEatenLegacyQueries,
    private val tagQueries: TagLegacyQueries,
    private val entryTagQueries: EntryTagLegacyQueries,
) : LegacyDao {

    private suspend inline fun <reified T: Any> getPreference(key: String): T? {
        return keyValueStore.read<T>(key).first()
    }

    override suspend fun getPreferences(): List<LegacyPreference> {
        val preferences = listOf(
            "versionCode".let { key ->
                LegacyPreference.Int(
                    key = key,
                    value = getPreference(key)!!,
                )
            },
            "theme".let { key ->
                LegacyPreference.String(
                    key = key,
                    value = getPreference(key)!!,
                )
            },
            // TODO: Add all legacy preferences
        )
        return preferences
    }

    override suspend fun getEntries(): List<Entry.Legacy> {
        return entryQueries.getAll()
    }

    override suspend fun getMeasurementValues(): List<MeasurementValue.Legacy> {
        return measurementValueQueries.getAll()
    }

    override suspend fun getFood(): List<Food.Legacy> {
        return foodQueries.getAll()
    }

    override suspend fun getFoodEaten(): List<FoodEaten.Legacy> {
        return foodEatenQueries.getAll()
    }

    override suspend fun getTags(): List<Tag.Legacy> {
        return tagQueries.getAll()
    }

    override suspend fun getEntryTags(): List<EntryTag.Legacy> {
        return entryTagQueries.getAll()
    }
}