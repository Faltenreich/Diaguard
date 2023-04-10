package com.faltenreich.rhyme.language.source

import com.faltenreich.rhyme.language.Language

interface LanguageDataSource {

    fun getCurrentLanguage(): Language
    fun setCurrentLanguage(language: Language)
}