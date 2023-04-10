package com.faltenreich.diaguard.language

import com.faltenreich.diaguard.language.source.LanguageDataSource
import kotlinx.coroutines.flow.MutableStateFlow

class LanguageRepository(private val dataSource: LanguageDataSource) {

    var currentLanguage = MutableStateFlow(dataSource.getCurrentLanguage())

    fun setLanguage(language: Language) {
        dataSource.setCurrentLanguage(language)
        currentLanguage.value = language
    }
}