package com.faltenreich.diaguard.startup.legacy

import com.faltenreich.diaguard.data.entry.Entry
import com.faltenreich.diaguard.data.entry.tag.EntryTag
import com.faltenreich.diaguard.data.food.Food
import com.faltenreich.diaguard.data.food.eaten.FoodEaten
import com.faltenreich.diaguard.data.measurement.value.MeasurementValue
import com.faltenreich.diaguard.preference.Preference
import com.faltenreich.diaguard.data.tag.Tag

class LegacyRepository(private val dao: LegacyDao) {

    suspend fun hasPreferences(): Boolean {
        return dao.hasPreferences()
    }

    suspend fun <Store: Any, Domain> getPreference(preference: Preference<Store, Domain>): Domain? {
        return dao.getPreference(preference)
    }

    suspend fun getEntries(): List<Entry.Legacy> {
        return dao.getEntries()
    }

    suspend fun getMeasurementValues(): List<MeasurementValue.Legacy> {
        return dao.getMeasurementValues()
    }

    suspend fun getFood(): List<Food.Legacy> {
        return dao.getFood()
    }

    suspend fun getFoodEaten(): List<FoodEaten.Legacy> {
        return dao.getFoodEaten()
    }

    suspend fun getTags(): List<Tag.Legacy> {
        return dao.getTags()
    }

    suspend fun getEntryTags(): List<EntryTag.Legacy> {
        return dao.getEntryTags()
    }
}