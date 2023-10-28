package com.faltenreich.diaguard.backup.seed.data

import com.faltenreich.diaguard.backup.seed.Seed
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.shared.file.FileReader

class FoodSeed(
    private val fileReader: FileReader,
) : Seed<Food> {

    override fun harvest(): Food {
        TODO("Not yet implemented")
    }
}