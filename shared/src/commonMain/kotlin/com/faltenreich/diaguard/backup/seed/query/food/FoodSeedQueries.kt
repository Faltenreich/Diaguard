package com.faltenreich.diaguard.backup.seed.query.food

import androidx.compose.ui.text.intl.Locale
import com.faltenreich.diaguard.backup.seed.query.SeedQueries
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.shared.file.FileReader
import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.localization.LocalizationConstants
import com.faltenreich.diaguard.shared.serialization.Serialization
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

        return dtoList.map { dto ->
            Food.Seed(
                name = dto.localizedName(locale),
                brand = null,
                ingredients = null,
                labels = labels,
                carbohydrates = dto.carbohydrates.toDouble(),
                energy = dto.energy.toDoubleOrNull(),
                fat = dto.fat.toDoubleOrNull(),
                fatSaturated = dto.fatSaturated.toDoubleOrNull(),
                fiber = dto.fiber.toDoubleOrNull(),
                proteins = dto.proteins.toDoubleOrNull(),
                salt = dto.salt.toDoubleOrNull(),
                sodium = dto.sodium.toDoubleOrNull(),
                sugar = dto.sugar.toDoubleOrNull(),
            )
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