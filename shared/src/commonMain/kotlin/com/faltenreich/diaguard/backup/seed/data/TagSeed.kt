package com.faltenreich.diaguard.backup.seed.data

import com.faltenreich.diaguard.backup.seed.SeedTag
import com.faltenreich.diaguard.shared.file.FileReader
import com.faltenreich.diaguard.shared.serialization.Serialization

class TagSeed(
    private val fileReader: FileReader,
    private val serialization: Serialization,
) {

    operator fun invoke(): List<SeedTag> {
        val csv = fileReader.read()
        return serialization.decodeCsv(csv)
    }
}