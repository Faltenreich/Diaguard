package com.faltenreich.diaguard.startup.legacy

import com.faltenreich.diaguard.data.entry.Entry
import com.faltenreich.diaguard.data.entry.tag.EntryTag
import com.faltenreich.diaguard.data.food.Food
import com.faltenreich.diaguard.data.food.eaten.FoodEaten
import com.faltenreich.diaguard.data.measurement.value.MeasurementValue
import com.faltenreich.diaguard.data.preference.Preference
import com.faltenreich.diaguard.data.tag.Tag

/**
 * Implementation is stubbed since there is no legacy on iOS to import from
 */
class EmptyLegacyDao : LegacyDao {

    override suspend fun hasPreferences(): Boolean {
        return false
    }

    override suspend fun <Store, Domain> getPreference(preference: Preference<Store, Domain>): Domain? {
        return null
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