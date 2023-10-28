package com.faltenreich.diaguard.backup.seed.data

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.backup.seed.Seed
import com.faltenreich.diaguard.backup.seed.SeedFood
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.file.FileReader
import com.faltenreich.diaguard.shared.file.ResourceFileReader
import com.faltenreich.diaguard.shared.serialization.Serialization

class FoodSeed(
    // TODO: Inject
    private val fileReader: FileReader = ResourceFileReader(MR.files.food_common),
    private val serialization: Serialization = inject(),
) : Seed<List<SeedFood>> {

    override fun harvest(): List<SeedFood> {
        val csv = fileReader.read()
        return serialization.decodeCsv(csv)
    }
}