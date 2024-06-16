package com.faltenreich.diaguard.backup.seed.query.tag

import com.faltenreich.diaguard.backup.seed.query.SeedQueries
import com.faltenreich.diaguard.shared.file.FileReader
import com.faltenreich.diaguard.shared.serialization.Serialization
import com.faltenreich.diaguard.tag.Tag

class TagSeedQueries(
    private val fileReader: FileReader,
    private val serialization: Serialization,
) : SeedQueries<Tag.Seed> {

    override fun getAll(): List<Tag.Seed> {
        val csv = fileReader.read()
        val dtos = serialization.decodeCsv<TagFromFile>(csv)
        return dtos.map { dto ->
            Tag.Seed(
                name = dto.en, // TODO: Localize
            )
        }
    }
}