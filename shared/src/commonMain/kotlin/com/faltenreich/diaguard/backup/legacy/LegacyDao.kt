package com.faltenreich.diaguard.backup.legacy

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.tag.EntryTag
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.eaten.FoodEaten
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.tag.Tag

interface LegacyDao {

    suspend fun getPreferences(): List<LegacyPreference>

    suspend fun getEntries(): List<Entry.Legacy>

    suspend fun getMeasurementValues(): List<MeasurementValue.Legacy>

    suspend fun getFood(): List<Food.Legacy>

    suspend fun getFoodEaten(): List<FoodEaten.Legacy>

    suspend fun getTags(): List<Tag.Legacy>

    suspend fun getEntryTags(): List<EntryTag.Legacy>
}