package com.faltenreich.rhyme.language

data class LanguageState(
    val languages: List<Language> = Language.values().toList(),
    val currentLanguage: Language,
)