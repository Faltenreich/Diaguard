package com.faltenreich.diaguard.backup.legacy

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.tag.EntryTag
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.eaten.FoodEaten
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.tag.Tag

class LegacyRepository(private val dao: LegacyDao) {

    fun getEntries(): List<Entry.Legacy> {
        return dao.getEntries()
    }

    fun getMeasurementValues(): List<MeasurementValue.Legacy> {
        return dao.getMeasurementValues()
    }

    fun getFood(): List<Food.Legacy> {
        return dao.getFood()
    }

    fun getFoodEaten(): List<FoodEaten.Legacy> {
        return dao.getFoodEaten()
    }

    fun getTags(): List<Tag.Legacy> {
        return dao.getTags()
    }

    fun getEntryTags(): List<EntryTag.Legacy> {
        return dao.getEntryTags()
    }
}