package com.faltenreich.diaguard.backup.legacy

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.tag.EntryTag
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.eaten.FoodEaten
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.tag.Tag

/**
 * Implementation is stubbed since there is no legacy on iOS to import from
 */
class EmptyLegacyDao : LegacyDao {

    override suspend fun getPreferences(): List<LegacyPreference> {
        return emptyList()
    }

    override suspend fun getEntries(): List<Entry.Legacy> {
        return emptyList()
    }

    override suspend fun getMeasurementValues(): List<MeasurementValue.Legacy> {
        return emptyList()
    }

    override suspend fun getFood(): List<Food.Legacy> {
        return emptyList()
    }

    override suspend fun getFoodEaten(): List<FoodEaten.Legacy> {
        return emptyList()
    }

    override suspend fun getTags(): List<Tag.Legacy> {
        return emptyList()
    }

    override suspend fun getEntryTags(): List<EntryTag.Legacy> {
        return emptyList()
    }
}