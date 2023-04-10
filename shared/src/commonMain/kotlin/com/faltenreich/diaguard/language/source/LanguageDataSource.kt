package com.faltenreich.diaguard.language.source

import com.faltenreich.diaguard.language.Language

interface LanguageDataSource {

    fun getCurrentLanguage(): Language
    fun setCurrentLanguage(language: Language)
}