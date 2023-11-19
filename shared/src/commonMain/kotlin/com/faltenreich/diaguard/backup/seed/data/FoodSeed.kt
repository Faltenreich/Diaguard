package com.faltenreich.diaguard.backup.seed.data

import com.faltenreich.diaguard.backup.seed.SeedFood
import com.faltenreich.diaguard.shared.file.FileReader
import com.faltenreich.diaguard.shared.serialization.Serialization

class FoodSeed(
    private val fileReader: FileReader,
    private val serialization: Serialization,
) {

    operator fun invoke(): List<SeedFood> {
        val csv = fileReader.read()
        return serialization.decodeCsv(csv)
    }
}