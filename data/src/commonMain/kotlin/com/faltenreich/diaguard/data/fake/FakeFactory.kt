package com.faltenreich.diaguard.data.fake

import com.faltenreich.diaguard.data.DatabaseKey
import com.faltenreich.diaguard.data.entry.Entry
import com.faltenreich.diaguard.data.entry.tag.EntryTag
import com.faltenreich.diaguard.data.food.Food
import com.faltenreich.diaguard.data.food.eaten.FoodEaten
import com.faltenreich.diaguard.data.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.data.measurement.property.MeasurementAggregationStyle
import com.faltenreich.diaguard.data.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.data.measurement.property.MeasurementValueRange
import com.faltenreich.diaguard.data.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.data.measurement.value.MeasurementValue
import com.faltenreich.diaguard.data.preview.PreviewDateTime
import com.faltenreich.diaguard.data.tag.Tag
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.datetime.DayOfWeek

object FakeFactory {

    fun now() = PreviewDateTime()

    fun today() = now().date

    @Suppress("MagicNumber")
    fun DayOfWeek.localized() = toString()
        .take(3)
        .lowercase()
        .replaceFirstChar(Char::uppercase)

    fun entry(
        id: Long = 0L,
        createdAt: DateTime = now(),
        updatedAt: DateTime = now(),
        dateTime: DateTime = now(),
        note: String = "Note",
        values: List<MeasurementValue.Local> = emptyList(),
        entryTags: List<EntryTag.Local> = emptyList(),
        foodEaten: List<FoodEaten.Local> = emptyList(),
    ) = Entry.Local(
        id = id,
        createdAt = createdAt,
        updatedAt = updatedAt,
        dateTime = dateTime,
        note = note,
    ).apply {
        this.values = values
        this.entryTags = entryTags
        this.foodEaten = foodEaten
    }

    fun category(
        id: Long = 0L,
        createdAt: DateTime = now(),
        updatedAt: DateTime = now(),
        name: String = "Category",
        icon: String = "Icon",
        sortIndex: Long = 0L,
        isActive: Boolean = true,
        key: DatabaseKey.MeasurementCategory? = null,
    ) = MeasurementCategory.Local(
        id = id,
        createdAt = createdAt,
        updatedAt = updatedAt,
        name = name,
        icon = icon,
        sortIndex = sortIndex,
        isActive = isActive,
        key = key,
    )

    fun property(
        id: Long = 0L,
        createdAt: DateTime = now(),
        updatedAt: DateTime = now(),
        name: String = "Property",
        sortIndex: Long = 0L,
        aggregationStyle: MeasurementAggregationStyle = MeasurementAggregationStyle.CUMULATIVE,
        range: MeasurementValueRange = MeasurementValueRange(
            minimum = 0.0,
            low = 80.0,
            target = 120.0,
            high = 180.0,
            maximum = 1_000.0,
            isHighlighted = true,
        ),
        category: MeasurementCategory.Local = category(),
        unit: MeasurementUnit.Local = unit(),
        key: DatabaseKey.MeasurementProperty? = null,
        valueFactor: Double = 1.0,
    ) = MeasurementProperty.Local(
        id = id,
        createdAt = createdAt,
        updatedAt = updatedAt,
        name = name,
        sortIndex = sortIndex,
        aggregationStyle = aggregationStyle,
        range = range,
        category = category,
        unit = unit,
        key = key,
        valueFactor = valueFactor,
    )

    fun unit(
        id: Long = 0L,
        createdAt: DateTime = now(),
        updatedAt: DateTime = now(),
        name: String = "Unit",
        abbreviation: String = "Unit",
        key: DatabaseKey.MeasurementUnit? = null,
    ) = MeasurementUnit.Local(
        id = id,
        createdAt = createdAt,
        updatedAt = updatedAt,
        name = name,
        abbreviation = abbreviation,
        key = key,
    )

    fun value(
        id: Long = 0L,
        createdAt: DateTime = now(),
        updatedAt: DateTime = now(),
        value: Double = 120.0,
        property: MeasurementProperty.Local = property(),
        entry: Entry.Local = entry(),
    ) = MeasurementValue.Local(
        id = id,
        createdAt = createdAt,
        updatedAt = updatedAt,
        value = value,
        property = property,
        entry = entry,
    )

    fun tag(
        id: Long = 0L,
        createdAt: DateTime = now(),
        updatedAt: DateTime = now(),
        name: String = "Unit",
    ) = Tag.Local(
        id = id,
        createdAt = createdAt,
        updatedAt = updatedAt,
        name = name,
    )

    fun food(
        id: Long = 0L,
        createdAt: DateTime = now(),
        updatedAt: DateTime = now(),
        uuid: String? = null,
        name: String = "Food",
        brand: String = "Brand",
        ingredients: String = "Ingredients",
        labels: String = "Labels",
        carbohydrates: Double = 5.0,
        energy: Double? = 2.0,
        fat: Double? = 2.0,
        fatSaturated: Double? = 2.0,
        fiber: Double? = 2.0,
        proteins: Double? = 2.0,
        salt: Double? = 2.0,
        sodium: Double? = 2.0,
        sugar: Double? = 2.0,
    ) = Food.Local(
        id = id,
        createdAt = createdAt,
        updatedAt = updatedAt,
        uuid = uuid,
        name = name,
        brand = brand,
        ingredients = ingredients,
        labels = labels,
        carbohydrates = carbohydrates,
        energy = energy,
        fat = fat,
        fatSaturated = fatSaturated,
        fiber = fiber,
        proteins = proteins,
        salt = salt,
        sodium = sodium,
        sugar = sugar,
    )

    fun Food.Local.localized(): Food.Localized {
        return Food.Localized(
            local = this,
            carbohydrates = carbohydrates.toString(),
            energy = energy?.toString(),
            fat = fat?.toString(),
            fatSaturated = fatSaturated?.toString(),
            fiber = fiber?.toString(),
            proteins = proteins?.toString(),
            salt = salt?.toString(),
            sodium = sodium?.toString(),
            sugar = sugar?.toString(),
        )
    }

    fun foodEaten(
        id: Long = 0L,
        createdAt: DateTime = now(),
        updatedAt: DateTime = now(),
        amountInGrams: Double = 20.0,
        food: Food.Local = food(),
        entry: Entry.Local = entry(),
    ) = FoodEaten.Local(
        id = id,
        createdAt = createdAt,
        updatedAt = updatedAt,
        amountInGrams = amountInGrams,
        food = food,
        entry = entry,
    )
}