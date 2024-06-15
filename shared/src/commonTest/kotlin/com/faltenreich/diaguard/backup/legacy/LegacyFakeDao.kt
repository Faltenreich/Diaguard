package com.faltenreich.diaguard.backup.legacy

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.tag.EntryTag
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.eaten.FoodEaten
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.tag.Tag

open class LegacyFakeDao : LegacyDao {

    override fun getEntries(): List<Entry.Legacy> {
        return emptyList()
    }

    override fun getMeasurementValues(): List<MeasurementValue.Legacy> {
        return emptyList()
    }


    override fun getFood(): List<Food.Legacy> {
        return emptyList()
    }

    override fun getFoodEaten(): List<FoodEaten.Legacy> {
        return emptyList()
    }

    override fun getTags(): List<Tag.Legacy> {
        return emptyList()
    }

    override fun getEntryTags(): List<EntryTag.Legacy> {
        return emptyList()
    }
}