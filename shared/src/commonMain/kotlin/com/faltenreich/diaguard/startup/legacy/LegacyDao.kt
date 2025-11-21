package com.faltenreich.diaguard.startup.legacy

import com.faltenreich.diaguard.data.entry.Entry
import com.faltenreich.diaguard.data.entry.tag.EntryTag
import com.faltenreich.diaguard.data.food.Food
import com.faltenreich.diaguard.data.food.eaten.FoodEaten
import com.faltenreich.diaguard.data.measurement.value.MeasurementValue
import com.faltenreich.diaguard.preference.Preference
import com.faltenreich.diaguard.data.tag.Tag

interface LegacyDao {

    suspend fun hasPreferences(): Boolean

    suspend fun <Store, Domain> getPreference(preference: Preference<Store, Domain>): Domain?

    suspend fun getEntries(): List<Entry.Legacy>

    suspend fun getMeasurementValues(): List<MeasurementValue.Legacy>

    suspend fun getFood(): List<Food.Legacy>

    suspend fun getFoodEaten(): List<FoodEaten.Legacy>

    suspend fun getTags(): List<Tag.Legacy>

    suspend fun getEntryTags(): List<EntryTag.Legacy>
}