package com.faltenreich.diaguard.onboarding

import com.faltenreich.diaguard.shared.file.FileReader
import com.faltenreich.diaguard.shared.serialization.Serialization

class SeedImport(
    private val fileReader: FileReader,
    private val serialization: Serialization,
) {

    fun import() {
        val yaml = fileReader.readFile("src/commonMain/resources/seed/properties.yml")
        val data = serialization.decodeYaml<List<Property>>(yaml)
        data.size
    }
}