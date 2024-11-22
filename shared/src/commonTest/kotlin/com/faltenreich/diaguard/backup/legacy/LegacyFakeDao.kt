package com.faltenreich.diaguard.backup.legacy

import com.faltenreich.diaguard.datetime.kotlinx.KotlinxDateTimeFactory
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.tag.EntryTag
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.eaten.FoodEaten
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.tag.Tag

class LegacyFakeDao : LegacyDao {

    private val dateTimeFactory = KotlinxDateTimeFactory()

    override suspend fun getPreferences(): Map<String, String> {
        TODO("Not yet implemented")
    }

    override suspend fun getEntries(): List<Entry.Legacy> {
        return listOf(
            Entry.Legacy(
                id = 1,
                createdAt = dateTimeFactory.dateTime(millis = 1717865198200),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865198200),
                dateTime = dateTimeFactory.dateTime(millis = 1715234400000),
                note = "Hello, World",
            ),
            Entry.Legacy(
                id = 2,
                createdAt = dateTimeFactory.dateTime(millis = 1717865198204),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865198204),
                dateTime = dateTimeFactory.dateTime(millis = 1717221600000),
                note = null,
            ),
            Entry.Legacy(
                id = 3,
                createdAt = dateTimeFactory.dateTime(millis = 1717865198208),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865198208),
                dateTime = dateTimeFactory.dateTime(millis = 1717308000000),
                note = null,
            ),
        )
    }

    override suspend fun getMeasurementValues(): List<MeasurementValue.Legacy> {
        return listOf(
            MeasurementValue.Legacy(
                id = 1,
                createdAt = dateTimeFactory.dateTime(millis = 1717865198203),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865198203),
                value = 100.0,
                propertyKey = DatabaseKey.MeasurementProperty.BLOOD_SUGAR,
                entryId = 1,
            ),
            MeasurementValue.Legacy(
                id = 2,
                createdAt = dateTimeFactory.dateTime(millis = 1717865198206),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865198206),
                value = 100.0,
                propertyKey = DatabaseKey.MeasurementProperty.BLOOD_SUGAR,
                entryId = 2,
            ),
            MeasurementValue.Legacy(
                id = 3,
                createdAt = dateTimeFactory.dateTime(millis = 1717865198208),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865198208),
                value = 120.0,
                propertyKey = DatabaseKey.MeasurementProperty.BLOOD_SUGAR,
                entryId = 3,
            ),
            MeasurementValue.Legacy(
                id = 1,
                createdAt = dateTimeFactory.dateTime(millis = 1717865198228),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865198228),
                value = 1.0,
                propertyKey = DatabaseKey.MeasurementProperty.INSULIN_BOLUS,
                entryId = 1,
            ),
            MeasurementValue.Legacy(
                id = 1,
                createdAt = dateTimeFactory.dateTime(millis = 1717865198228),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865198228),
                value = 0.0,
                propertyKey = DatabaseKey.MeasurementProperty.INSULIN_CORRECTION,
                entryId = 1,
            ),
            MeasurementValue.Legacy(
                id = 1,
                createdAt = dateTimeFactory.dateTime(millis = 1717865198228),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865198228),
                value = 0.0,
                propertyKey = DatabaseKey.MeasurementProperty.INSULIN_BASAL,
                entryId = 1,
            ),
            MeasurementValue.Legacy(
                id = 2,
                createdAt = dateTimeFactory.dateTime(millis = 1717865198246),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865198246),
                value = 6.0,
                propertyKey = DatabaseKey.MeasurementProperty.INSULIN_BOLUS,
                entryId = 2,
            ),
            MeasurementValue.Legacy(
                id = 2,
                createdAt = dateTimeFactory.dateTime(millis = 1717865198246),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865198246),
                value = 0.0,
                propertyKey = DatabaseKey.MeasurementProperty.INSULIN_CORRECTION,
                entryId = 2,
            ),
            MeasurementValue.Legacy(
                id = 2,
                createdAt = dateTimeFactory.dateTime(millis = 1717865198246),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865198246),
                value = 0.0,
                propertyKey = DatabaseKey.MeasurementProperty.INSULIN_BASAL,
                entryId = 2,
            ),
            MeasurementValue.Legacy(
                id = 1,
                createdAt = dateTimeFactory.dateTime(millis = 1717865198221),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865198221),
                value = 0.0,
                propertyKey = DatabaseKey.MeasurementProperty.MEAL,
                entryId = 1,
            ),
            MeasurementValue.Legacy(
                id = 2,
                createdAt = dateTimeFactory.dateTime(millis = 1717865198244),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865198244),
                value = 60.0,
                propertyKey = DatabaseKey.MeasurementProperty.MEAL,
                entryId = 2,
            ),
            MeasurementValue.Legacy(
                id = 1,
                createdAt = dateTimeFactory.dateTime(millis = 1717865198232),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865198232),
                value = 30.0,
                propertyKey = DatabaseKey.MeasurementProperty.ACTIVITY,
                entryId = 2,
            ),
            MeasurementValue.Legacy(
                id = 1,
                createdAt = dateTimeFactory.dateTime(millis = 1717865198241),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865198241),
                value = 90.0,
                propertyKey = DatabaseKey.MeasurementProperty.PULSE,
                entryId = 3,
            ),
        )
    }


    override suspend fun getFood(): List<Food.Legacy> {
        return listOf(
            Food.Legacy(
                id = 1,
                createdAt = dateTimeFactory.dateTime(millis = 1717865198076),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865198076),
                uuid = null,
                name = "Onion quiche, baked (with cake dough)",
                brand = null,
                ingredients = "Onion quiche, baked (with cake dough)",
                labels = "Common food",
                carbohydrates = 15.0,
                energy = 175.0,
                fat = 10.0,
                fatSaturated = 5.0,
                fiber = 1.6,
                proteins = 5.0,
                salt = 4.1,
                sodium = 0.4,
                sugar = 5.1,
            ),
            Food.Legacy(
                id = 962,
                createdAt = dateTimeFactory.dateTime(millis = 1717865198194),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865198194),
                uuid = null,
                name = "Cola",
                brand = "",
                ingredients = "",
                labels = null,
                carbohydrates = 10.0,
                energy = -1.0,
                fat = -1.0,
                fatSaturated = -1.0,
                fiber = -1.0,
                proteins = -1.0,
                salt = -1.0,
                sodium = -1.0,
                sugar = -1.0,
            ),
        )
    }

    override suspend fun getFoodEaten(): List<FoodEaten.Legacy> {
        return listOf(
            FoodEaten.Legacy(
                createdAt = dateTimeFactory.dateTime(millis = 1717865198225),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865198225),
                amountInGrams = 100.0,
                foodId = 962,
                mealId = 1,
            ),
        )
    }

    override suspend fun getTags(): List<Tag.Legacy> {
        return listOf(
            Tag.Legacy(
                id = 1,
                createdAt = dateTimeFactory.dateTime(millis = 1717865197888),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865197888),
                name = "after the sport",
            ),
            Tag.Legacy(
                id = 2,
                createdAt = dateTimeFactory.dateTime(millis = 1717865197898),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865197898),
                name = "before the sport",
            ),
            Tag.Legacy(
                id = 3,
                createdAt = dateTimeFactory.dateTime(millis = 1717865197898),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865197898),
                name = "sick",
            ),
            Tag.Legacy(
                id = 4,
                createdAt = dateTimeFactory.dateTime(millis = 1717865197899),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865197899),
                name = "tired",
            ),
            Tag.Legacy(
                id = 5,
                createdAt = dateTimeFactory.dateTime(millis = 1717865197899),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865197899),
                name = "excited",
            ),
            Tag.Legacy(
                id = 6,
                createdAt = dateTimeFactory.dateTime(millis = 1717865197899),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865197899),
                name = "happy",
            ),
            Tag.Legacy(
                id = 7,
                createdAt = dateTimeFactory.dateTime(millis = 1717865197899),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865197899),
                name = "falling asleep",
            ),
            Tag.Legacy(
                id = 8,
                createdAt = dateTimeFactory.dateTime(millis = 1717865197899),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865197899),
                name = "just woke up",
            ),
        )
    }

    override suspend fun getEntryTags(): List<EntryTag.Legacy> {
        return listOf(
            EntryTag.Legacy(
                createdAt = dateTimeFactory.dateTime(millis = 1717865198200),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865198200),
                entryId = 1,
                tagId = 1,
            )
        )
    }
}