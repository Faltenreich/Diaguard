package com.faltenreich.diaguard.data.preview

import com.faltenreich.diaguard.data.entry.Entry
import com.faltenreich.diaguard.data.food.Food
import com.faltenreich.diaguard.data.food.eaten.FoodEaten
import com.faltenreich.diaguard.data.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.data.measurement.property.MeasurementAggregationStyle
import com.faltenreich.diaguard.data.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.data.measurement.property.MeasurementValueRange
import com.faltenreich.diaguard.data.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.data.measurement.value.MeasurementValue
import com.faltenreich.diaguard.data.tag.Tag
import com.faltenreich.diaguard.datetime.DateProgression
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.DayOfWeek

class PreviewScope {

    fun now() = PreviewDateTime()

    fun today() = now().date

    fun week() = today().let { today ->
        DateProgression(
            start = today
                .minus(1, DateUnit.WEEK)
                .plus(1, DateUnit.DAY),
            endInclusive = today,
        )
    }

    fun entry() = Entry.Local(
        id = 0L,
        createdAt = now(),
        updatedAt = now(),
        dateTime = now(),
        note = "Note",
    ).apply {
        values = emptyList()
        entryTags = emptyList()
        foodEaten = emptyList()
    }

    fun category() = MeasurementCategory.Local(
        id = 0L,
        createdAt = now(),
        updatedAt = now(),
        name = "Category",
        icon = "Icon",
        sortIndex = 0L,
        isActive = true,
        key = null,
    )

    fun property() = MeasurementProperty.Local(
        id = 0L,
        createdAt = now(),
        updatedAt = now(),
        name = "Property",
        sortIndex = 0L,
        aggregationStyle = MeasurementAggregationStyle.CUMULATIVE,
        range = MeasurementValueRange(
            minimum = 0.0,
            low = 80.0,
            target = 120.0,
            high = 180.0,
            maximum = 1_000.0,
            isHighlighted = true,
        ),
        category = category(),
        unit = unit(),
        key = null,
        valueFactor = 1.0,
    )

    fun unit() = MeasurementUnit.Local(
        id = 0L,
        createdAt = now(),
        updatedAt = now(),
        name = "Unit",
        abbreviation = "Unit",
        key = null,
    )

    fun value() = MeasurementValue.Local(
        id = 0L,
        createdAt = now(),
        updatedAt = now(),
        value = 120.0,
        property = property(),
        entry = entry(),
    )

    fun tag() = Tag.Local(
        id = 0L,
        createdAt = now(),
        updatedAt = now(),
        name = "Tag",
    )

    fun food() = Food.Local(
        id = 0L,
        createdAt = now(),
        updatedAt = now(),
        uuid = null,
        name = "Food",
        brand = "Brand",
        ingredients = "Ingredients",
        labels = "Labels",
        carbohydrates = 5.0,
        energy = 2.0,
        fat = 2.0,
        fatSaturated = 2.0,
        fiber = 2.0,
        proteins = 2.0,
        salt = 2.0,
        sodium = 2.0,
        sugar = 2.0,
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

    fun foodEaten() = FoodEaten.Local(
        id = 0L,
        createdAt = now(),
        updatedAt = now(),
        amountInGrams = 20.0,
        food = food(),
        entry = entry(),
    )

    @Suppress("MagicNumber")
    fun DayOfWeek.localized() = toString()
        .take(3)
        .lowercase()
        .replaceFirstChar(Char::uppercase)
}