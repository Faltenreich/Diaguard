package com.faltenreich.diaguard.backup.seed.data

import com.faltenreich.diaguard.backup.seed.SeedFood
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.shared.file.FileReader
import com.faltenreich.diaguard.shared.serialization.Serialization

class FoodSeed(
    private val fileReader: FileReader,
    private val serialization: Serialization,
) {

    operator fun invoke(): List<Food.Seed> {
        val csv = fileReader.read()
        val dtos = serialization.decodeCsv<SeedFood>(csv)
        return dtos.map { dto ->
            Food.Seed(
                name = dto.en, // TODO: Localize
                brand = null,
                ingredients = null,
                labels = null, // TODO: Mark seed
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
}