package com.faltenreich.diaguard.backup.legacy

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.tag.EntryTag
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.eaten.FoodEaten
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.preference.Preference
import com.faltenreich.diaguard.tag.Tag

class LegacyRepository(private val dao: LegacyDao) {

    suspend fun <Store, Domain> getPreference(preference: Preference<Store, Domain>): Domain? {
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