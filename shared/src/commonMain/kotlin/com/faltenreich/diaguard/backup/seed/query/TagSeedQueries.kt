package com.faltenreich.diaguard.backup.seed.query

import com.faltenreich.diaguard.backup.seed.SeedTag
import com.faltenreich.diaguard.shared.file.FileReader
import com.faltenreich.diaguard.shared.serialization.Serialization
import com.faltenreich.diaguard.tag.Tag

class TagSeedQueries(
    private val fileReader: FileReader,
    private val serialization: Serialization,
) : SeedQueries<Tag.Seed> {

    override fun getAll(): List<Tag.Seed> {
        val csv = fileReader.read()
        val dtos = serialization.decodeCsv<SeedTag>(csv)
        return dtos.map { dto ->
            Tag.Seed(
                name = dto.en, // TODO: Localize
            )
        }
    }
}