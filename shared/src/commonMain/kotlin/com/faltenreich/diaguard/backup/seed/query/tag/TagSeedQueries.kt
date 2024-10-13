package com.faltenreich.diaguard.backup.seed.query.tag

import androidx.compose.ui.text.intl.Locale
import com.faltenreich.diaguard.backup.seed.query.SeedQueries
import com.faltenreich.diaguard.shared.file.FileReader
import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.localization.LocalizationConstants
import com.faltenreich.diaguard.shared.serialization.Serialization
import com.faltenreich.diaguard.tag.Tag

class TagSeedQueries(
    private val fileReader: FileReader,
    private val serialization: Serialization,
    private val localization: Localization,
) : SeedQueries<Tag.Seed> {

    override fun getAll(): List<Tag.Seed> {
        val csv = fileReader.read()
        val dtoList = serialization.decodeCsv<TagFromFile>(csv)

        val locale = localization.getLocale()

        return dtoList.map { dto ->
            Tag.Seed(name = dto.localizedName(locale))
        }
    }

    private fun TagFromFile.localizedName(locale: Locale): String {
        return when (locale.language) {
            LocalizationConstants.LANGUAGE_FRENCH -> fr
            LocalizationConstants.LANGUAGE_GERMAN -> de
            LocalizationConstants.LANGUAGE_ITALIAN -> it
            LocalizationConstants.LANGUAGE_SPANISH -> es
            else -> en
        }
    }
}