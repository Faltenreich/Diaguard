package com.faltenreich.diaguard.backup.legacy

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.tag.EntryTag
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.eaten.FoodEaten
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.tag.Tag

interface LegacyDao {

    fun getEntries(): List<Entry.Legacy>

    fun getMeasurementValues(): List<MeasurementValue.Legacy>

    fun getFood(): List<Food.Legacy>

    fun getFoodEaten(): List<FoodEaten.Legacy>

    fun getTags(): List<Tag.Legacy>

    fun getEntryTags(): List<EntryTag.Legacy>
}