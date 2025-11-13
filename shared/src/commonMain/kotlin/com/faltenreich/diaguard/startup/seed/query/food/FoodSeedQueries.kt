package com.faltenreich.diaguard.startup.seed.query.food

import androidx.compose.ui.text.intl.Locale
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.shared.file.FileReader
import com.faltenreich.diaguard.core.localization.Localization
import com.faltenreich.diaguard.core.localization.LocalizationConstants
import com.faltenreich.diaguard.shared.serialization.Serialization
import com.faltenreich.diaguard.startup.seed.query.SeedQueries
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.food_common

class FoodSeedQueries(
    private val fileReader: FileReader,
    private val serialization: Serialization,
    private val localization: Localization,
) : SeedQueries<Food.Seed> {

    override fun getAll(): List<Food.Seed> {
        val csv = fileReader.read()
        val dtoList = serialization.decodeCsv<FoodFromFile>(csv)

        val locale = localization.getLocale()
        val labels = localization.getString(Res.string.food_common)

        return dtoList.mapNotNull { dto ->
            with(dto) {
                val carbohydrates = carbohydrates?.toDoubleOrNull() ?: return@mapNotNull null
                Food.Seed(
                    name = localizedName(locale),
                    brand = null,
                    ingredients = null,
                    labels = labels,
                    carbohydrates = carbohydrates,
                    energy = energy?.toDoubleOrNull(),
                    fat = fat?.toDoubleOrNull(),
                    fatSaturated = fatSaturated?.toDoubleOrNull(),
                    fiber = fiber?.toDoubleOrNull(),
                    proteins = proteins?.toDoubleOrNull(),
                    salt = salt?.toDoubleOrNull(),
                    sodium = sodium?.toDoubleOrNull(),
                    sugar = sugar?.toDoubleOrNull(),
                )
            }
        }
    }

    private fun FoodFromFile.localizedName(locale: Locale): String {
        return when (locale.language) {
            LocalizationConstants.LANGUAGE_FRENCH -> fr
            LocalizationConstants.LANGUAGE_GERMAN -> de
            LocalizationConstants.LANGUAGE_ITALIAN -> it
            else -> en
        }
    }
}