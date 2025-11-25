package com.faltenreich.diaguard.data.legacy

import com.faltenreich.diaguard.data.entry.Entry
import com.faltenreich.diaguard.data.entry.tag.EntryTag
import com.faltenreich.diaguard.data.food.Food
import com.faltenreich.diaguard.data.food.eaten.FoodEaten
import com.faltenreich.diaguard.data.legacy.query.EntryLegacyQueries
import com.faltenreich.diaguard.data.legacy.query.EntryTagLegacyQueries
import com.faltenreich.diaguard.data.legacy.query.FoodEatenLegacyQueries
import com.faltenreich.diaguard.data.measurement.value.MeasurementValue
import com.faltenreich.diaguard.data.preference.Preference
import com.faltenreich.diaguard.data.legacy.query.FoodLegacyQueries
import com.faltenreich.diaguard.data.legacy.query.KeyValueLegacyQueries
import com.faltenreich.diaguard.data.legacy.query.MeasurementValueLegacyQueries
import com.faltenreich.diaguard.data.legacy.query.TagLegacyQueries
import com.faltenreich.diaguard.data.tag.Tag

class AndroidLegacyDao(
    private val keyValueQueries: KeyValueLegacyQueries,
    private val entryQueries: EntryLegacyQueries,
    private val measurementValueQueries: MeasurementValueLegacyQueries,
    private val foodQueries: FoodLegacyQueries,
    private val foodEatenQueries: FoodEatenLegacyQueries,
    private val tagQueries: TagLegacyQueries,
    private val entryTagQueries: EntryTagLegacyQueries,
) : LegacyDao {

    override suspend fun hasPreferences(): Boolean {
        return keyValueQueries.hasPreferences()
    }

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