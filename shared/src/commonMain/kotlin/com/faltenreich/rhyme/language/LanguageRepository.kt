package com.faltenreich.rhyme.language

import com.faltenreich.rhyme.language.source.LanguageDataSource
import kotlinx.coroutines.flow.MutableStateFlow

class LanguageRepository(private val dataSource: LanguageDataSource) {

    var currentLanguage = MutableStateFlow(dataSource.getCurrentLanguage())

    fun setLanguage(language: Language) {
        dataSource.setCurrentLanguage(language)
        currentLanguage.value = language
    }
}