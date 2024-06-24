package com.faltenreich.diaguard.backup.legacy

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.tag.EntryTag
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.eaten.FoodEaten
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.tag.Tag

expect class LegacySqliteDao : LegacyDao {

    override fun getEntries(): List<Entry.Legacy>

    override fun getMeasurementValues(): List<MeasurementValue.Legacy>

    override fun getFood(): List<Food.Legacy>

    override fun getFoodEaten(): List<FoodEaten.Legacy>

    override fun getTags(): List<Tag.Legacy>

    override fun getEntryTags(): List<EntryTag.Legacy>
}