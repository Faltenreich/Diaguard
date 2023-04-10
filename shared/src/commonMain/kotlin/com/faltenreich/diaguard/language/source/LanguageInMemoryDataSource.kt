package com.faltenreich.diaguard.language.source

import com.faltenreich.diaguard.language.Language
import com.faltenreich.diaguard.shared.localization.Localization

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