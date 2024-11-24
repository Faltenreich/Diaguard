package com.faltenreich.diaguard.backup.legacy

import com.faltenreich.diaguard.backup.legacy.query.EntryLegacyQueries
import com.faltenreich.diaguard.backup.legacy.query.EntryTagLegacyQueries
import com.faltenreich.diaguard.backup.legacy.query.FoodEatenLegacyQueries
import com.faltenreich.diaguard.backup.legacy.query.FoodLegacyQueries
import com.faltenreich.diaguard.backup.legacy.query.KeyValueLegacyQueries
import com.faltenreich.diaguard.backup.legacy.query.MeasurementValueLegacyQueries
import com.faltenreich.diaguard.backup.legacy.query.TagLegacyQueries
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.tag.EntryTag
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.eaten.FoodEaten
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.preference.Preference
import com.faltenreich.diaguard.tag.Tag

class AndroidLegacyDao(
    private val keyValueQueries: KeyValueLegacyQueries,
    private val entryQueries: EntryLegacyQueries,
    private val measurementValueQueries: MeasurementValueLegacyQueries,
    private val foodQueries: FoodLegacyQueries,
    private val foodEatenQueries: FoodEatenLegacyQueries,
    private val tagQueries: TagLegacyQueries,
    private val entryTagQueries: EntryTagLegacyQueries,
) : LegacyDao {

    override suspend fun <Store, Domain> getPreference(preference: Preference<Store, Domain>): Domain? {
        return keyValueQueries.getPreference(preference)
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