package com.faltenreich.diaguard.backup.seed.query.food

import com.faltenreich.diaguard.backup.seed.query.SeedQueries
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.shared.file.FileReader
import com.faltenreich.diaguard.shared.serialization.Serialization

class FoodSeedQueries(
    private val fileReader: FileReader,
    private val serialization: Serialization,
) : SeedQueries<Food.Seed> {

    override fun getAll(): List<Food.Seed> {
        val csv = fileReader.read()
        val dtos = serialization.decodeCsv<FoodFromFile>(csv)
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