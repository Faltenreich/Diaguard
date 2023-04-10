package com.faltenreich.rhyme.language.source

import com.faltenreich.rhyme.language.Language
import com.faltenreich.rhyme.shared.localization.Localization

class LanguageInMemoryDataSource(private val localization: Localization): LanguageDataSource {

    private var currentLanguage: Language = getSystemLanguage()

    override fun getCurrentLanguage(): Language {
        return currentLanguage
    }

    override fun setCurrentLanguage(language: Language) {
        currentLanguage = language
    }

    private fun getSystemLanguage(): Language {
        return Language.fromLanguageCode(localization.getLanguageCode()) ?: Language.default
    }
}